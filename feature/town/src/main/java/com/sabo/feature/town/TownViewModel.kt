package com.sabo.feature.town

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.data.Result
import com.sabo.core.data.handle
import com.sabo.core.data.repository.TownRepository
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
            townContent = TownContent(isLoading = true, isNewUser = false, dataList = emptyList())
        ),
        onCreate = {
            fetchTownGrowth()
        }
    )

    private fun fetchTownGrowth() = intent {
        viewModelScope.launch {
            reduce {
                state.copy(
                    townContent = TownContent(isLoading = true, isNewUser = false, dataList = emptyList())
                )
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

                val isNewUser = isNewUserResult.data.growths.isEmpty()

                reduce {
                    state.copy(
                        townContent = TownContent(
                            isLoading = false,
                            dataList = newList,
                            isNewUser = isNewUser
                        )
                    )
                }
            }
        }
    }

    fun loadMoreTownGrowth(lastId: Long) = intent {
        when (val result = townRepository.getTownGrowth(lastId)) {
            is Result.Success -> {
                val userId = townRepository.getUserId()

                val existingIds = state.townContent.dataList
                    .filterIsInstance<TownListItem.Post>()
                    .map { it.id }
                    .toSet()

                val newGrowths = result.data.growths.filter { it.growthId !in existingIds }

                val newTownItems = state.townContent.dataList.toMutableList().apply {
                    removeAt(lastIndex)
                    addAll(newGrowths.map { it.toPresentation(userId = userId) })
                    if (newGrowths.isNotEmpty()) add(TownListItem.LoadMore(lastId = newGrowths.last().growthId))
                }

                reduce {
                    state.copy(
                        townContent = state.townContent.copy(dataList = newTownItems)
                    )
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
                state.copy(
                    townContent = TownContent(isLoading = true, isNewUser = false, dataList = emptyList())
                )
            }

            when (val result = townRepository.getMyGrowth()) {
                is Result.Success -> {
                    val townItems = result.data.growths.map { growth -> growth.toPresentation(isMine = true) }
                    val isNewUser = townItems.isEmpty()

                    reduce {
                        state.copy(
                            townContent = TownContent(
                                isLoading = false,
                                dataList = townItems,
                                isNewUser = isNewUser
                            )
                        )
                    }
                }
                is Result.Error -> {
                    reduce {
                        state.copy(
                            townContent = TownContent(
                                isLoading = false,
                                dataList = emptyList(),
                                isNewUser = true
                            )
                        )
                    }
                }
            }
        }
    }
}
