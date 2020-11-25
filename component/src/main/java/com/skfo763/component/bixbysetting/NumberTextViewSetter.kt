package com.skfo763.component.bixbysetting

class NumberTextViewSetter(
    private val viewList: List<NumberIconTextView> = listOf(),
    private val checkPointList: List<Int> = listOf()
) {

    fun onVideoTimeMillis(videoTimeMillis: Int, maxVideoTime: Int) {
        if (viewList.size != 4 || checkPointList.size != 4) return
        when (videoTimeMillis / 1000) {
            in checkPointList[0]..checkPointList[1] -> {
                highlightNumberText(0)
            }
            in checkPointList[1]..checkPointList[2] -> {
                highlightNumberText(1)
            }
            in checkPointList[2]..checkPointList[3] -> {
                highlightNumberText(2)
            }
            in checkPointList[3]..maxVideoTime -> {
                highlightNumberText(3)
            }
        }
    }

    private fun highlightNumberText(index: Int) {
        if (index < 0 || viewList.size != 4) return
        when (index) {
            0 -> {
                viewList[0].setViewState(NumberIconTextView.ViewState.ON_AIR)
                viewList[1].setViewState(NumberIconTextView.ViewState.PLAIN)
                viewList[2].setViewState(NumberIconTextView.ViewState.PLAIN)
                viewList[3].setViewState(NumberIconTextView.ViewState.PLAIN)
            }
            1 -> {
                viewList[0].setViewState(NumberIconTextView.ViewState.PLAIN)
                viewList[1].setViewState(NumberIconTextView.ViewState.ON_AIR)
                viewList[2].setViewState(NumberIconTextView.ViewState.PLAIN)
                viewList[3].setViewState(NumberIconTextView.ViewState.PLAIN)
            }
            2 -> {
                viewList[0].setViewState(NumberIconTextView.ViewState.PLAIN)
                viewList[1].setViewState(NumberIconTextView.ViewState.PLAIN)
                viewList[2].setViewState(NumberIconTextView.ViewState.ON_AIR)
                viewList[3].setViewState(NumberIconTextView.ViewState.PLAIN)
            }
            3 -> {
                viewList[0].setViewState(NumberIconTextView.ViewState.PLAIN)
                viewList[1].setViewState(NumberIconTextView.ViewState.PLAIN)
                viewList[2].setViewState(NumberIconTextView.ViewState.PLAIN)
                viewList[3].setViewState(NumberIconTextView.ViewState.ON_AIR)
            }
        }
    }
}