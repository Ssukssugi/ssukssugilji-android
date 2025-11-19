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
                        imageList = result.byMonth
                            .sortedWith(compareBy({ it.year }, { it.month }))
                            .flatMap { monthData ->
                                monthData.diaries
                                    .groupBy { it.date }
                                    .toSortedMap()
                                    .flatMap { (_, diaries) -> diaries.reversed() }
                            }
                            .map { diary ->
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
        val clickedIndex = state.imageList.indexOfFirst { it.diaryId == historyImage.diaryId }
        if (clickedIndex == -1) return@intent

        val beforeIndex = state.beforeImage?.let { before ->
            state.imageList.indexOfFirst { it.diaryId == before.diaryId }
        } ?: -1

        val afterIndex = state.afterImage?.let { after ->
            state.imageList.indexOfFirst { it.diaryId == after.diaryId }
        } ?: -1

        when {
            beforeIndex == -1 && afterIndex == -1 -> {
                selectImage(historyImage, VariationImageType.BEFORE)
            }

            beforeIndex != -1 && afterIndex == -1 -> {
                when {
                    clickedIndex == beforeIndex -> resetImage(VariationImageType.BEFORE)
                    clickedIndex < beforeIndex -> resetAndSelectImage(VariationImageType.BEFORE, historyImage)
                    else -> selectImage(historyImage, VariationImageType.AFTER)
                }
            }

            beforeIndex != -1 && afterIndex != -1 -> {
                when {
                    clickedIndex == beforeIndex -> resetAllImages()
                    clickedIndex == afterIndex -> resetImage(VariationImageType.AFTER)
                    clickedIndex < beforeIndex -> resetAndSelectImage(VariationImageType.BEFORE, historyImage)
                    clickedIndex > afterIndex -> resetAndSelectImage(VariationImageType.AFTER, historyImage)
                    else -> resetAndSelectImage(VariationImageType.AFTER, historyImage)
                }
            }
        }
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

    private fun resetAndSelectImage(type: VariationImageType, newImage: HistoryImage) = intent {
        val imageUrlToReset = when (type) {
            VariationImageType.BEFORE -> state.beforeImage?.url
            VariationImageType.AFTER -> state.afterImage?.url
        }

        reduce {
            state.copy(
                beforeImage = if (type == VariationImageType.BEFORE) newImage else state.beforeImage,
                afterImage = if (type == VariationImageType.AFTER) newImage else state.afterImage,
                imageList = state.imageList.map { image ->
                    when (image.url) {
                        imageUrlToReset -> image.copy(selectedType = null)
                        newImage.url -> image.copy(selectedType = type)
                        else -> image
                    }
                }
            )
        }
    }

    private fun resetAllImages() = intent {
        val beforeUrl = state.beforeImage?.url
        val afterUrl = state.afterImage?.url

        reduce {
            state.copy(
                beforeImage = null,
                afterImage = null,
                imageList = state.imageList.map { image ->
                    if (image.url == beforeUrl || image.url == afterUrl) {
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