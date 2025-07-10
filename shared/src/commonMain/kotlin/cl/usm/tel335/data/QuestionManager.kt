package cl.usm.tel335.data

import cl.usm.tel335.model.QuestionDto

object QuestionManager {
    val sampleQuestionList = mutableListOf(
        QuestionDto(
            id = 1,
            text = "Cual es la capital de Chile?",
            category = "Geografía"
        ),
        QuestionDto(
            id = 2,
            text = "Cual es la capital de Argentina?",
            category = "Geografía"
        ),
        QuestionDto(
            id = 3,
            text = "Quién escribió el Quijote de la Mancha?",
            category = "Literatura"
        )
    )
}