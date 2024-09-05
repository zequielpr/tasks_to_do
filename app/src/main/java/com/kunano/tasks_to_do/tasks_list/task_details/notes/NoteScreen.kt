package com.kunano.tasks_to_do.tasks_list.task_details.notes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.utils.customBasicTextField
import com.kunano.tasks_to_do.core.utils.navigateBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navigateBack: () -> Unit,
    paddingValues: PaddingValues,
    noteViewModel: NoteViewModel = hiltViewModel()
) {
    val noteScreenState by noteViewModel.noteScreenState.collectAsStateWithLifecycle()

    Column {
        TopAppBar(title = { }, navigationIcon = {
            navigateBackButton(navigateBack = navigateBack)
        })
        noteScreenContent(
            paddingValues = paddingValues,
            noteScreenState = noteScreenState,
            onContentChange = noteViewModel::onContentChange,
            onTitleChanges = noteViewModel::onTitleChange
        )
    }


}

@Composable
fun noteScreenContent(
    paddingValues: PaddingValues,
    noteScreenState: NoteScreenState,
    onTitleChanges: (value: String) -> Unit,
    onContentChange: (value: String) -> Unit
) {

    Column(modifier = Modifier.padding(20.dp, 0.dp)) {
        customBasicTextField(
            textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier,
            value = noteScreenState.noteTitle ?: "",
            hint = R.string.note_title,
            onValueChange = onTitleChanges
        )


        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {

            noteScreenState.lastModified?.let {
                val textStyle = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(0.dp, 20.dp),
                ) {
                    Text(
                        style = textStyle,
                        modifier = Modifier.padding(end = 10.dp),
                        text = stringResource(id = R.string.last_modified)
                    )
                    Text(

                        style = textStyle,
                        text = it
                    )
                }
            }

            Card(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 20.dp)
            ) {
                Box(modifier = Modifier.padding(20.dp)) {
                    customBasicTextField(
                        singleLine = false,
                        modifier = Modifier,
                        value = noteScreenState.note ?: "",
                        hint = R.string.note_content,
                        onValueChange = onContentChange
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun noteScreenContentPreview() {
    noteScreenContent(
        paddingValues = PaddingValues(0.dp),
        noteScreenState = NoteScreenState("Title", "Content", lastModified = "12/12/2024"),
        onTitleChanges = {},
        onContentChange = {})
}