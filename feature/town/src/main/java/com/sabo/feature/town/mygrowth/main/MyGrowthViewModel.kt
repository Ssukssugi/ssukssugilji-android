package com.sabo.feature.town.mygrowth.main

import androidx.lifecycle.ViewModel
import com.sabo.core.data.handle
import com.sabo.core.data.repository.TownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyGrowthViewModel @Inject constructor(
    private val townRepository: TownRepository
) : ContainerHost<MyGrowthState, MyGrowthSideEffect>, ViewModel() {

    override val container: Container<MyGrowthState, MyGrowthSideEffect> = container(
        initialState = MyGrowthState(isLoading = true),
        onCreate = {
            fetchGrowths()
        }
    )

    private fun fetchGrowths() = intent {
        reduce { state.copy(isLoading = true) }
        townRepository.getMyGrowth().handle(
            onSuccess = { result ->
                reduce {
                    state.copy(
                        growthList = result.growths.map { it.toPresentation() },
                        isEmpty = result.growths.isEmpty()
                    )
                }
            },
            onFinish = {
                reduce {
                    state.copy(isLoading = false)
                }
            }
        )
    }
}