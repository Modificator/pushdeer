package com.pushdeer.os.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hivemq.client.mqtt.MqttClient
import com.pushdeer.os.store.SettingStore
import java.nio.charset.StandardCharsets

class MQTTViewModel(private val settingStore: SettingStore) : ViewModel() {
    var inputMQTTHost by mutableStateOf("")
    var inputMQTTPort by mutableStateOf("")
    var inputMQTTUser by mutableStateOf("")
    var inputMQTTPassword by mutableStateOf("")
    var inputMQTTTopic by mutableStateOf("")

    fun saveAndConnect() {
        settingStore.mqttHost = inputMQTTHost
        settingStore.mqttPort = inputMQTTPort
        settingStore.mqttUser = inputMQTTUser
        settingStore.mqttPassword = inputMQTTPassword
        settingStore.mqttTopic = inputMQTTTopic
    }

    fun tryConnect() {
        val builder = MqttClient.builder()
            .useMqttVersion5()
            .identifier(settingStore.thisDeviceId) //设备唯一标识
        if (inputMQTTHost != "")
            builder.serverHost(inputMQTTHost)
        if (inputMQTTPort != "")
            builder.serverPort(inputMQTTPort.toInt())
        if (inputMQTTUser != "")
            builder.simpleAuth()
                .username(inputMQTTUser)
                .password(inputMQTTPassword.toByteArray(StandardCharsets.UTF_8))
                .applySimpleAuth()
                .addConnectedListener {
                    Log.e("8paw9w6ccx", "onconnect")
                }
                .addDisconnectedListener {
                    Log.e("8paw9w6ccx", "ondisconnect")
                }
                .build()
    }
}