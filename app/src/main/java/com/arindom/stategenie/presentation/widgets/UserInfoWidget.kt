/*
 * Copyright 2023 Arindom Ghosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arindom.stategenie.presentation.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arindom.stategenie.data.model.User

@Composable
fun UserInfo(
  modifier: Modifier,
  user: User
) {
  Row(
    modifier = modifier
      .padding(20.dp)
  ) {
    AsyncImage(
      model = user.avatar_url,
      contentDescription = "User Image",
      modifier = modifier.size(40.dp, 40.dp)
    )
    Text(
      text = user.login,
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
    user = User(
      login = "jclarke",
      avatar_url = "https://avatars.githubusercontent.com/u/2022?v=4"
    )
  )
}
