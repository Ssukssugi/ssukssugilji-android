package com.sabo.feature.town

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.data.Result
import com.sabo.core.data.handle
import com.sabo.core.data.repository.TownRepository
import com.sabo.core.model.NetworkErrorEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TownViewModel @Inject constructor(
    private val townRepository: TownRepository
) : ContainerHost<TownUiState, TownEvent>, ViewModel() {

    override val container: Container<TownUiState, TownEvent> = container(
        initialState = TownUiState(
            townContent = TownContent.Loading
        ),
        onCreate = {
            fetchTownGrowth()
        }
    )

    private fun fetchTownGrowth() = intent {
        viewModelScope.launch {
            reduce {
                state.copy(townContent = TownContent.Loading)
            }
            val userIdDeferred = async { townRepository.getUserId() }
            val growthDeferred = async { townRepository.getTownGrowth(null) }
            val isNewUserDeferred = async { townRepository.getMyGrowth() }

            val growthResult = growthDeferred.await()
            val isNewUserResult = isNewUserDeferred.await()

            if (growthResult is Result.Success && isNewUserResult is Result.Success) {
                val townItems = growthResult.data.growths.map { growth -> growth.toPresentation(userId = userIdDeferred.await()) }

                val newList = if (townItems.isNotEmpty()) {
                    townItems + TownListItem.LoadMore(lastId = townItems.last().id)
                } else {
                    townItems
                }

                reduce {
                    state.copy(
                        townContent = if (newList.isEmpty()) TownContent.Empty else TownContent.Data(dataList = newList)
                    )
                }
            } else {
                reduce {
                    state.copy(townContent = TownContent.NetworkError(NetworkErrorEvent.NoInternet))
                }
            }
        }
    }

    fun loadMoreTownGrowth(lastId: Long) = intent {
        val currentContent = state.townContent
        if (currentContent !is TownContent.Data) return@intent

        when (val result = townRepository.getTownGrowth(lastId)) {
            is Result.Success -> {
                val userId = townRepository.getUserId()

                val existingIds = currentContent.dataList
                    .filterIsInstance<TownListItem.Post>()
                    .map { it.id }
                    .toSet()

                val newGrowths = result.data.growths.filter { it.growthId !in existingIds }

                val newTownItems = currentContent.dataList.toMutableList().apply {
                    removeAt(lastIndex)
                    addAll(newGrowths.map { it.toPresentation(userId = userId) })
                    if (newGrowths.isNotEmpty()) add(TownListItem.LoadMore(lastId = newGrowths.last().growthId))
                }

                reduce {
                    state.copy(townContent = TownContent.Data(dataList = newTownItems))
                }
            }

            is Result.Error -> {}
        }
    }

    fun onClickGrowthPostMore(item: TownListItem.Post) = intent {
        postSideEffect(TownEvent.ShowPostOptions(SelectedGrowth(growthId = item.id, isMine = item.isMine)))
    }

    fun reportGrowthPost(id: Long) = intent {
        reduce { state.copy(isLoading = true) }
        townRepository.reportTown(growthId = id).handle(
            onSuccess = {
                postSideEffect(TownEvent.ShowSnackBarReportGrowth)
            },
            onFinish = {
                reduce { state.copy(isLoading = false) }
            }
        )
    }

    fun deleteGrowthPost(id: Long) = intent {
        reduce { state.copy(isLoading = true) }
        townRepository.deleteGrowth(growthId = id).handle(
            onSuccess = {
                when (state.selectedTab) {
                    TownTab.ALL -> fetchTownGrowth()
                    TownTab.MY_POSTS -> fetchMyGrowth()
                }
                postSideEffect(TownEvent.ShowSnackBarDeleteGrowth)
            },
            onFinish = {
                reduce { state.copy(isLoading = false) }
            }
        )
    }

    fun onTabSelected(tab: TownTab) = intent {
        if (state.selectedTab == tab) return@intent

        reduce { state.copy(selectedTab = tab) }

        when (tab) {
            TownTab.ALL -> fetchTownGrowth()
            TownTab.MY_POSTS -> fetchMyGrowth()
        }
    }

    private fun fetchMyGrowth() = intent {
        viewModelScope.launch {
            reduce {
                state.copy(townContent = TownContent.Loading)
            }

            when (val result = townRepository.getMyGrowth()) {
                is Result.Success -> {
                    val townItems = result.data.growths.map { growth -> growth.toPresentation(isMine = true) }

                    reduce {
                        state.copy(
                            townContent = if (townItems.isEmpty()) TownContent.Empty else TownContent.Data(dataList = townItems)
                        )
                    }
                }
                is Result.Error -> {
                    reduce {
                        state.copy(townContent = TownContent.NetworkError(NetworkErrorEvent.NoInternet))
                    }
                }
            }
        }
    }

    fun onRetryClicked() = intent {
        when (state.selectedTab) {
            TownTab.ALL -> fetchTownGrowth()
            TownTab.MY_POSTS -> fetchMyGrowth()
        }
    }
}
