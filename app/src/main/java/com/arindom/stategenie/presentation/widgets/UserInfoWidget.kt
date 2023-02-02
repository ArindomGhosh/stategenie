package com.arindom.stategenie.presentation.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arindom.stategenie.Greeting
import com.arindom.stategenie.data.model.UserResponse

@Composable
fun UserInfo(
    modifier: Modifier,
    userResponse: UserResponse
) {
    Row(
        modifier = modifier
            .padding(20.dp)
    ) {
        AsyncImage(
            model = userResponse.avatar_url,
            contentDescription = "User Image",
            modifier = modifier.size(40.dp, 40.dp)
        )
        Text(
            text = userResponse.login,
            fontSize = 16.sp,
            modifier = modifier
                .padding(start = 30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPreview() {
    UserInfo(
        modifier = Modifier,
        userResponse = UserResponse(
            login = "jclarke",
            avatar_url = "https://avatars.githubusercontent.com/u/2022?v=4"
        )
    )
}