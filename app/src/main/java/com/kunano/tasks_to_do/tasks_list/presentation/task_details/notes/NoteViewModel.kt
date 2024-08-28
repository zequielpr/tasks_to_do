package com.kunano.tasks_to_do.tasks_list.presentation.task_details.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(): ViewModel() {
    private val _noteScreenState: MutableStateFlow<NoteScreenState> = MutableStateFlow(NoteScreenState())

    val noteScreenState: StateFlow<NoteScreenState> = _noteScreenState

    fun fetchNote(taskKey: String){
        viewModelScope.launch {
            _noteScreenState.update { currentState ->
                currentState.copy(noteTitle = "note title: $taskKey", note = "The first banknote-type instrument was used in China in the 7th century, during the Tang dynasty (618–907). Merchants would issue what are today called promissory notes in the form of receipts of deposit to wholesalers to avoid using the heavy bulk of copper coinage in large commercial transactions.[13][14][15] Before these notes, circular coins with a rectangular hole in the middle were used. Multiple coins could be strung together on a rope. Merchants found that the strings were too heavy to carry around easily, especially for large transactions. To solve this problem, coins could be left with a trusted person, with the merchant being given a slip of paper (the receipt) recording how much money they had deposited with that person. When they returned with the paper to that person, their coins would be returned.\n" +
                        "\n" +
                        "True paper money, called \"jiaozi\", developed from these promissory notes by the 11th century, during the Song dynasty.[20][21] By 960, the Song government was short of copper for striking coins, and issued the first generally circulating notes. These notes were a promise by the ruler to redeem them later for some other object of value, usually specie. The issue of credit notes was often for a limited duration, and at some discount to the promised amount later. The jiaozi did not replace coins but was used alongside them.\n" +
                        "\n" +
                        "The central government soon observed the economic advantages of printing paper money, issuing a monopoly for the issue of these certificates of deposit to several deposit shops.[13] By the early 12th century, the amount of banknotes issued in a single year amounted to an annual rate of 26 million strings of cash coins.[15] By the 1120s the central government started to produce its own state-issued paper money (using woodblock printing).[13]\n" +
                        "\n" +
                        "Even before this point, the Song government was amassing large amounts of paper tribute. It was recorded that each year before 1101, the prefecture of Xin'an (modern Shexian, Anhui) alone would send 1,500,000 sheets of paper in seven different varieties to the capital at Kaifeng.[22] In 1101, the Emperor Huizong of Song decided to lessen the amount of paper taken in the tribute quota, because it was causing detrimental effects and creating heavy burdens on the people of the region.[23] However, the government still needed masses of paper product for the exchange certificates and the state's new issuing of paper money. For the printing of paper money alone, the Song government established several government-run factories in the cities of Huizhou,[which?] Chengdu, Hangzhou, and Anqi.[23]\n" +
                        "\n" +
                        "The workforce employed in these paper money factories was quite large; it was recorded in 1175 that the factory at Hangzhou alone employed more than a thousand workers a day.[23] However, the government issues of paper money were not yet nationwide standards of currency at that point; issues of banknotes were limited to regional areas of the empire, and were valid for use only in a designated and temporary limit of three years.[15]\n" +
                        "\n" +
                        "The geographic limitation changed between 1265 and 1274, when the late southern Song government issued a nationwide paper currency standard, which was backed by gold or silver.[15] The range of varying values for these banknotes was perhaps from one string of cash to one hundred at the most.[15] Ever after 1107, the government printed money in no less than six ink colors and printed notes with intricate designs and sometimes even with mixture of a unique fiber in the paper to combat counterfeiting.\n" +
                        "\n" +
                        "The founder of the Yuan dynasty, Kublai Khan, issued paper money known as Jiaochao. The original notes were restricted by area and duration, as in the Song dynasty, but in the later years, facing massive shortages of specie to fund their rule, the paper money began to be issued without restrictions on duration. Venetian merchants were impressed by the fact that the Chinese paper money was guaranteed by the State.")
            }
        }
    }
}