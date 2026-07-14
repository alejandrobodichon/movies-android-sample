package com.example.ui.model

import com.example.testing.sampleMovie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ToUiModelTest {

    @Test
    fun `toUiModel maps all fields correctly`() {
        val movie = sampleMovie(1)

        val uiModel = movie.toUiModel()

        assertEquals(movie.id, uiModel.id)
        assertEquals(movie.poster, uiModel.poster)
        assertEquals(movie.title, uiModel.title)
        assertEquals(movie.releaseDate, uiModel.releaseDate)
        assertEquals(movie.overview, uiModel.overview)
        assertEquals(movie.backdrop, uiModel.backdrop)
        assertEquals(movie.originalTitle, uiModel.originalTitle)
        assertEquals(movie.originalLanguage, uiModel.originalLanguage)
        assertEquals(movie.popularity, uiModel.popularity, 0.0)
        assertEquals(movie.voteAverage, uiModel.voteAverage, 0.0)
    }

    @Test
    fun `toUiModel maps null backdrop correctly`() {
        val movie = sampleMovie(1).copy(backdrop = null)

        val uiModel = movie.toUiModel()

        assertNull(uiModel.backdrop)
    }
}
