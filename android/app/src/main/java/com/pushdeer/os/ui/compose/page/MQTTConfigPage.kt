package com.pushdeer.os.ui.compose.page

import android.text.InputType
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushdeer.os.R
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.compose.page.main.MainPageFrame
import com.pushdeer.os.ui.theme.MBlue
import com.pushdeer.os.ui.theme.MainBlue

@Composable
fun MQTTConfigPage(requestHolder: RequestHolder) {
    MainPageFrame(titleStringId = R.string.main_setting_mqtt_config, showSideIcon = false) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            MQTTConfigItem(
                hint = stringResource(id = R.string.mqtt_config_host),
                editValue = requestHolder.mqttViewModel.inputMQTTHost,
                onValueChange = {
                    requestHolder.mqttViewModel.inputMQTTHost = it
                }
            )
            MQTTConfigItem(
                hint = stringResource(id = R.string.mqtt_config_port),
                editValue = requestHolder.mqttViewModel.inputMQTTPort,
                inputType = KeyboardType.Number,
                onValueChange = {
                    requestHolder.mqttViewModel.inputMQTTPort = it
                }
            )
            MQTTConfigItem(
                hint = stringResource(id = R.string.mqtt_config_user),
                editValue = requestHolder.mqttViewModel.inputMQTTUser,
                onValueChange = {
                    requestHolder.mqttViewModel.inputMQTTUser = it
                }
            )
            MQTTConfigItem(
                hint = stringResource(id = R.string.mqtt_config_pasword),
                editValue = requestHolder.mqttViewModel.inputMQTTPassword,
                onValueChange = {
                    requestHolder.mqttViewModel.inputMQTTPassword = it
                }
            )
            MQTTConfigItem(
                hint = stringResource(id = R.string.mqtt_config_topic),
                editValue = requestHolder.mqttViewModel.inputMQTTTopic,
                onValueChange = {
                    requestHolder.mqttViewModel.inputMQTTTopic = it
                }
            )
            Row {
                Button(
                    onClick = {
                        requestHolder.mqttViewModel.saveAndConnect()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.MBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.mqtt_config_saveapply),
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        requestHolder.globalNavController.navigateUp()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.MBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.mqtt_config_cancel),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MQTTConfigItem(
    hint: String, editValue: String,
    inputType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = editValue,
        onValueChange = onValueChange,
        label = { Text(text = hint, color = MainBlue) },
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colors.MBlue,
            focusedLabelColor = Color.Transparent,
            unfocusedBorderColor = MaterialTheme.colors.MBlue,
            unfocusedLabelColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = inputType),
        maxLines = 1,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    )
}