package com.android.sopkathon_android1.feature.hyemin

import androidx.lifecycle.SavedStateHandle
import com.android.sopkathon_android1.data.datasource.SelectedImage
import com.android.sopkathon_android1.data.datasource.SelectedImageSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminReviewDataSource
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminVerificationScenario
import com.android.sopkathon_android1.data.repository.fake.FakeHyeminRepository
import com.android.sopkathon_android1.data.model.UploadedImage
import com.android.sopkathon_android1.data.repository.ImageRepository
import com.android.sopkathon_android1.data.repository.MissionCompletionRepository
import com.android.sopkathon_android1.data.repository.MissionBasketRepository
import com.android.sopkathon_android1.data.repository.ReviewRepository
import com.android.sopkathon_android1.data.model.MissionVerificationResult
import com.android.sopkathon_android1.data.repository.remote.ImageUploadException
import com.android.sopkathon_android1.data.repository.remote.MissionCompletionException
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HyeminDetailViewModelTest {
    @Before fun setup() { Dispatchers.setMain(StandardTestDispatcher()) }
    @After fun cleanup() { Dispatchers.resetMain() }

    private var imageUploads = 0
    private var uploadFails = false
    private var completionError: MissionCompletionException? = null
    private var lastCompletion: Triple<Long, String, String>? = null
    private val imageRepository = object : ImageRepository {
        override suspend fun uploadImage(file: File, mimeType: String): UploadedImage {
            imageUploads++
            if (uploadFails) throw ImageUploadException(413, "IMAGE_SIZE_EXCEEDED")
            return UploadedImage("uploaded-image-key", "https://example.com/image.jpg")
        }
    }
    private val selectedImageSource = object : SelectedImageSource {
        override suspend fun read(uri: String): SelectedImage = SelectedImage(
            File.createTempFile("selected-image-", ".jpg"),
            "image/jpeg",
        )
    }
    private fun viewModel(repository: FakeHyeminRepository, id: String): HyeminDetailViewModel {
        val basketRepository = object : MissionBasketRepository {
            override suspend fun getMissions() = repository.getMissions()
        }
        val completionRepository = object : MissionCompletionRepository {
            override suspend fun completeMission(missionBasketId: Long, imageKey: String, content: String): MissionVerificationResult {
                lastCompletion = Triple(missionBasketId, imageKey, content)
                completionError?.let { throw it }
                val missionId = repository.getMissions().first { it.missionBasketId == missionBasketId }.missionId
                return repository.completeMission(missionId.toString(), imageKey, content, false)
            }
        }
        return HyeminDetailViewModel(
            repository,
            basketRepository,
            imageRepository,
            completionRepository,
            selectedImageSource,
            SavedStateHandle(mapOf("missionBasketId" to id)),
        )
    }

    @Test fun `목록에서 전달한 id로 상세를 조회하고 저장한 초안을 다시 연다`() = runTest {
        val repository = FakeHyeminRepository(FakeHyeminDataSource(), FakeHyeminReviewDataSource())
        val first = viewModel(repository, "13")
        advanceUntilIdle()
        assertEquals("4", first.uiState.value.mission?.missionId?.toString())
        assertEquals("먼작귀 극장판을 보고 새로운 즐거움을 발견해보세요.", first.uiState.value.description)
        assertEquals("1. 관람할 상영 시간 확인\n2. 영화 관람\n3. 아래에 사진과 후기를 등록", first.uiState.value.instructions)
        first.review.edit { append("다음에 이어서 작성") }
        first.setPhoto("content://photo")
        first.saveDraft()
        advanceUntilIdle()
        assertTrue(first.uiState.value.finished)

        val reopened = viewModel(repository, "13")
        advanceUntilIdle()
        assertEquals("다음에 이어서 작성", reopened.review.text.toString())
        assertEquals("content://photo", reopened.uiState.value.photo)
    }

    @Test fun `상세 완료 후 목록을 재조회하면 완료 상태가 반영된다`() = runTest {
        val repository = FakeHyeminRepository(FakeHyeminDataSource(), FakeHyeminReviewDataSource())
        val list = HyeminViewModel(
            repository,
            object : MissionBasketRepository {
                override suspend fun getMissions() = repository.getMissions()
            },
            object : ReviewRepository {
                override suspend fun getReviews() = repository.getReviews()
            },
        )
        val detail = viewModel(repository, "12")
        advanceUntilIdle()
        assertEquals("1. 셀프 사진관 또는 휴대폰 준비\n2. 좋아하는 표정으로 네 컷 촬영\n3. 아래에 사진과 후기를 등록", detail.uiState.value.instructions)
        assertEquals("좋아하는 표정으로 나만의 네 컷 사진을 남겨보세요.", detail.uiState.value.description)
        detail.setPhoto("content://photo")
        detail.review.edit { append("재미있었어요") }
        detail.completeMission()
        assertEquals(HyeminVerificationState.LOADING, detail.uiState.value.verificationState)
        assertTrue(detail.uiState.value.submitting)
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.SUCCESS, detail.uiState.value.verificationState)
        assertEquals(1, imageUploads)
        assertEquals(Triple(12L, "uploaded-image-key", "재미있었어요"), lastCompletion)
        assertFalse(detail.uiState.value.finished)
        detail.completeMission() // 성공 Toast 표시 중 재전송하지 않는다.
        assertEquals(HyeminVerificationState.SUCCESS, detail.uiState.value.verificationState)
        detail.dismissVerificationToast()
        assertTrue(detail.uiState.value.finished)
        assertFalse(detail.uiState.value.canInteract)
        list.loadMissions()
        advanceUntilIdle()
        assertTrue(list.uiState.value.missions.first { it.missionId == 3L }.completed)
    }
    @Test fun `FAIL 판정이어도 완료 상태가 저장되고 화면을 닫는다`() = runTest {
        val source = FakeHyeminDataSource()
        val vm = viewModel(FakeHyeminRepository(source, FakeHyeminReviewDataSource()), "12")
        advanceUntilIdle()
        vm.setPhoto("content://photo")
        vm.review.edit { append("재미있었어요") }
        source.verificationScenario = FakeHyeminVerificationScenario.FAILURE
        vm.completeMission()
        vm.completeMission() // 판독 중 중복 요청은 무시한다.
        assertEquals(HyeminVerificationState.LOADING, vm.uiState.value.verificationState)
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.FAILURE, vm.uiState.value.verificationState)
        assertEquals("content://photo", vm.uiState.value.photo)
        assertEquals("재미있었어요", vm.review.text.toString())
        assertFalse(vm.uiState.value.finished)
        assertTrue(source.isCompleted(3))
        assertTrue(vm.uiState.value.completionSaved)
        assertFalse(vm.uiState.value.canInteract)
        vm.dismissVerificationToast()
        assertEquals(HyeminVerificationState.IDLE, vm.uiState.value.verificationState)
        assertTrue(vm.uiState.value.finished)
    }

    @Test fun `통신 실패도 실패 Toast 상태로 표시한다`() = runTest {
        val source = FakeHyeminDataSource()
        val vm = viewModel(FakeHyeminRepository(source, FakeHyeminReviewDataSource()), "12")
        advanceUntilIdle()
        source.verificationScenario = FakeHyeminVerificationScenario.ERROR
        vm.setPhoto("content://photo")
        vm.review.edit { append("후기") }
        vm.completeMission()
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.FAILURE, vm.uiState.value.verificationState)
        assertTrue(vm.uiState.value.canInteract)
        assertFalse(vm.uiState.value.finished)
        assertFalse(source.isCompleted(3))
        vm.dismissVerificationToast()
        source.verificationScenario = FakeHyeminVerificationScenario.SUCCESS
        vm.completeMission()
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.SUCCESS, vm.uiState.value.verificationState)
        assertEquals(1, imageUploads)
    }

    @Test fun `필수 입력값이 없으면 인증을 요청하지 않는다`() = runTest {
        val vm = viewModel(FakeHyeminRepository(FakeHyeminDataSource(), FakeHyeminReviewDataSource()), "12")
        advanceUntilIdle()
        vm.completeMission()
        assertEquals(HyeminVerificationState.IDLE, vm.uiState.value.verificationState)
        vm.setPhoto("content://photo")
        vm.review.edit { append("   ") }
        vm.completeMission()
        assertEquals(HyeminVerificationState.IDLE, vm.uiState.value.verificationState)
    }

    @Test fun `이미지 업로드 실패 시 인증하지 않고 재시도할 수 있다`() = runTest {
        val source = FakeHyeminDataSource()
        val vm = viewModel(FakeHyeminRepository(source, FakeHyeminReviewDataSource()), "12")
        advanceUntilIdle()
        vm.setPhoto("content://photo")
        vm.review.edit { append("후기") }
        uploadFails = true
        vm.completeMission()
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.FAILURE, vm.uiState.value.verificationState)
        assertEquals("이미지는 최대 10MB까지 업로드할 수 있어요.", vm.uiState.value.message)
        assertFalse(source.isCompleted(3))
        assertEquals("content://photo", vm.uiState.value.photo)
        vm.dismissVerificationToast()
        uploadFails = false
        vm.completeMission()
        advanceUntilIdle()
        assertEquals(HyeminVerificationState.SUCCESS, vm.uiState.value.verificationState)
        assertEquals(2, imageUploads)
    }

    @Test fun `이미 완료된 미션 응답이면 목록에 완료를 반영하고 닫는다`() = runTest {
        val source = FakeHyeminDataSource()
        val vm = viewModel(FakeHyeminRepository(source, FakeHyeminReviewDataSource()), "12")
        advanceUntilIdle()
        vm.setPhoto("content://photo")
        vm.review.edit { append("후기") }
        completionError = MissionCompletionException(409, "MISSION_ALREADY_COMPLETED")
        vm.completeMission()
        advanceUntilIdle()
        assertTrue(source.isCompleted(3))
        assertTrue(vm.uiState.value.completionSaved)
        vm.dismissVerificationToast()
        assertTrue(vm.uiState.value.finished)
    }
}
