package cl.usm.tel335

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.usm.tel335.data.QuestionManager
import cl.usm.tel335.model.QuestionDto
import questionapp.composeapp.generated.resources.Res

@Composable
@Preview(
    showBackground = true
)
fun App() {
    MaterialTheme {
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ){
            items(QuestionManager.sampleQuestionList) { q ->
                QuestionItem(questionDto = q)
            }
        }
    }
}

@Composable
fun QuestionList(questions: List<QuestionDto>) {
    LazyColumn (
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ){
        items(questions) { q ->
            QuestionItem(questionDto = q)
        }
    }
}

@Composable
fun QuestionItem(questionDto: QuestionDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                questionDto.text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = questionDto.category,
                fontSize = 16.sp,
                color = MaterialTheme.colors.primary,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun QuestionItemPreview() {
    QuestionItem(QuestionManager.sampleQuestionList[0])
}


