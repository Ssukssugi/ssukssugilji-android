package com.sabo.feature.town.mygrowth.variation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.sabo.core.data.handle
import com.sabo.core.data.repository.DiaryRepository
import com.sabo.core.data.repository.TownRepository
import com.sabo.core.navigator.model.GrowthVariation
import com.sabo.feature.town.mygrowth.variation.component.VariationImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class VariationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diaryRepository: DiaryRepository,
    private val townRepository: TownRepository
) : ContainerHost<VariationState, VariationSideEffect>, ViewModel() {

    private val args = savedStateHandle.toRoute<GrowthVariation>()

    override val container: Container<VariationState, VariationSideEffect> = container(
        initialState = VariationState(isLoading = true, plantId = args.plantId, plantNickname = args.plantName),
        onCreate = { fetchHistoryImages(args.plantId) }
    )

    private fun fetchHistoryImages(plantId: Long) = intent {
        diaryRepository.getPlantDiaries(plantId = plantId).handle(
            onSuccess = { result ->
                reduce {
                    state.copy(
                        beforeImage = null,
                        afterImage = null,
                        imageList = result.byMonth.flatMap { it.diaries }.map { diary ->
                            HistoryImage(
                                url = diary.image,
                                selectedType = null,
                                diaryId = diary.diaryId
                            )
                        }
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

    fun onImageClick(historyImage: HistoryImage) = intent {
        val currentState = state

        if (historyImage.selectedType != null) {
            resetImage(historyImage.selectedType)
            return@intent
        }

        val typeToSelect = when {
            currentState.beforeImage == null -> VariationImageType.BEFORE
            currentState.afterImage == null -> VariationImageType.AFTER
            else -> return@intent
        }

        selectImage(historyImage, typeToSelect)
    }

    private fun selectImage(historyImage: HistoryImage, type: VariationImageType) = intent {
        reduce {
            state.copy(
                beforeImage = if (type == VariationImageType.BEFORE) historyImage else state.beforeImage,
                afterImage = if (type == VariationImageType.AFTER) historyImage else state.afterImage,
                imageList = state.imageList.map { image ->
                    if (image.url == historyImage.url) {
                        image.copy(selectedType = type)
                    } else {
                        image
                    }
                }
            )
        }
    }

    private fun resetImage(type: VariationImageType) = intent {
        val imageUrlToReset = when (type) {
            VariationImageType.BEFORE -> state.beforeImage?.url
            VariationImageType.AFTER -> state.afterImage?.url
        }

        reduce {
            state.copy(
                beforeImage = if (type == VariationImageType.BEFORE) null else state.beforeImage,
                afterImage = if (type == VariationImageType.AFTER) null else state.afterImage,
                imageList = state.imageList.map { image ->
                    if (image.url == imageUrlToReset) {
                        image.copy(selectedType = null)
                    } else {
                        image
                    }
                }
            )
        }
    }

    fun requestToSaveGrowth() = intent {
        val before = state.beforeImage ?: return@intent
        val after = state.afterImage ?: return@intent
        reduce { state.copy(isLoading = true) }
        townRepository.saveGrowth(beforeId = before.diaryId, afterId = after.diaryId).handle(
            onSuccess = {
                postSideEffect(VariationSideEffect.NavigateToHomeWithSuccess)
            },
            onFinish = {
                reduce { state.copy(isLoading = false) }
            }
        )
    }
}