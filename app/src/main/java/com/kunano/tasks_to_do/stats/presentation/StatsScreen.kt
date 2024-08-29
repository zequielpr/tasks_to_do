package com.kunano.tasks_to_do.stats.presentation

import Route
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kunano.tasks_to_do.R

@Composable
fun StatsScreen(
    paddingValues: PaddingValues,
    navigate: (route: Route) -> Unit,
    statsViewModel: StatsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.padding(20.dp)
) {

    val statsScreenState by statsViewModel.statsScreenState.collectAsStateWithLifecycle()


    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize().padding(paddingValues)) {

        tasksStats(statsScreenState = statsScreenState, modifier = modifier)
    }

}


@Composable
fun tasksStats(statsScreenState: StatsScreenState, modifier: Modifier) {
    val quantityTextStyle: TextStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp)

    val horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally

    val verticalArrangement: Arrangement.Vertical = Arrangement.Center
    Row(horizontalArrangement = Arrangement.spacedBy(18.dp), modifier = modifier) {

        Card(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalAlignment = horizontalAlignment,
                verticalArrangement = verticalArrangement
            ) {
                Text(
                    text = statsScreenState.completedTask,
                    modifier = modifier,
                    style = quantityTextStyle
                )
                Text(text = stringResource(id = R.string.completed_tasks))
            }
        }
        Card(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalAlignment = horizontalAlignment,
                verticalArrangement = verticalArrangement//
            ) {
                Text(
                    text = statsScreenState.pendingTask,
                    modifier = modifier,
                    style = quantityTextStyle
                )
                Text(text = stringResource(id = R.string.pending_tasks))
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    StatsScreen(paddingValues = PaddingValues(), navigate = {})
}