package com.example.first_app.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.first_app.presentation.Dimens.PageIndicatorWidth
import com.example.first_app.presentation.Dimens.mediumPadding2
import com.example.first_app.presentation.common.NewsButton
import com.example.first_app.presentation.common.NewsTextButton
import com.example.first_app.presentation.onboarding.components.OnBoardingPage
import com.example.first_app.presentation.onboarding.components.PageIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun OnBoardingScreen(
    OnEvent: (OnBoardingEvent)->Unit ={}
){
    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val pagerState = rememberPagerState(initialPage = 0){pages.size}

            val buttonState = remember{
                derivedStateOf {
                    when(pagerState.currentPage){
                        0->listOf("","Next")
                        1->listOf("Back","Next")
                        2->listOf("Back","Get Started")
                        else->listOf("","")
                    }
                }
            }
            HorizontalPager(state = pagerState) {
                    index->  OnBoardingPage(page = pages[index])
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = mediumPadding2)
                        .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                PageIndicator(modifier = Modifier
                    .width(PageIndicatorWidth),
                    pageSize = pages.size,
                    selectedPage = pagerState.currentPage)

            Row(
               verticalAlignment = Alignment.CenterVertically
            ){
                val scope = rememberCoroutineScope()
                if(buttonState.value[0].isNotEmpty()){
                    NewsTextButton(
                        text = buttonState.value[0],
                        onClick = {
                                    scope.launch {
                                       pagerState.animateScrollToPage(page = pagerState.currentPage-1)
                                    }
                        }
                    )
                }

                NewsButton(
                    text = buttonState.value[1],
                    onClick = {
                        scope.launch {
                            if(pagerState.currentPage == 2){
                                    OnEvent(OnBoardingEvent.SaveAppEntry)
                            }else{
                                pagerState.animateScrollToPage(page = pagerState.currentPage+1)
                            }
                        }
                    }
                )
            }
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}