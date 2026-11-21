package com.android.sopkathon_android1.data.repository.fake

import com.android.sopkathon_android1.data.model.HyeminMissionDraft
import com.android.sopkathon_android1.data.datasource.fake.FakeHyeminDataSource
import com.android.sopkathon_android1.data.repository.HyeminRepository
import javax.inject.Inject

class FakeHyeminRepository @Inject constructor(private val dataSource: FakeHyeminDataSource) : HyeminRepository {
    private val drafts = mutableMapOf<String, HyeminMissionDraft>()
    override suspend fun getDraft(id: String) = drafts[id]
    override suspend fun saveDraft(id: String, draft: HyeminMissionDraft) {
        drafts[id] = draft
    }
    override suspend fun getMissions() = dataSource.getMissions()
    override suspend fun completeMission(id: String, photo: String, review: String, liked: Boolean) {
        dataSource.completeMission(id, photo, review)
        drafts.remove(id)
    }
}
