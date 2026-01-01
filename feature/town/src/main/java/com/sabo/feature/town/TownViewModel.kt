package com.sabo.feature.town

import android.util.Log
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
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
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

            val growthDeferred = async { townRepository.getTownGrowth(null) }
            val isNewUserDeferred = async { townRepository.getMyGrowth() }

            val growthResult = growthDeferred.await()
            val isNewUserResult = isNewUserDeferred.await()

            if (growthResult is Result.Success && isNewUserResult is Result.Success) {
                val townItems = growthResult.data.growths.map { growth -> growth.toPresentation() }

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
                val existingIds = state.townContent.dataList
                    .filterIsInstance<TownListItem.Post>()
                    .map { it.id }
                    .toSet()

                val newGrowths = result.data.growths.filter { it.growthId !in existingIds }

                val newTownItems = state.townContent.dataList.toMutableList().apply {
                    removeAt(lastIndex)
                    addAll(newGrowths.map { it.toPresentation() })
                    if (newGrowths.isNotEmpty()) add(TownListItem.LoadMore(lastId = newGrowths.last().growthId))
                }

                reduce {
                    state.copy(
                        townContent = state.townContent.copy(dataList = newTownItems)
                    )
                }
            }

            is Result.Error -> {
                Log.d("lololo", "${result.message}")
            }
        }
    }

    fun onClickGrowthPostMore(id: Long) = intent {
        postSideEffect(TownEvent.ShowPostOptions(growthId = id))
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
                    val townItems = result.data.growths.map { growth -> growth.toPresentation() }
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
