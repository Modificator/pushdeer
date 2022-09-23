package com.pushdeer.os.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.pushdeer.os.App
import com.pushdeer.os.R
import com.pushdeer.os.store.SettingStore
import java.nio.charset.StandardCharsets

class MQTTService : Service() {
    val notificationChannelId = "high_system"
    val settingStore: SettingStore by lazy { (application as App).storeKeeper.settingStore }

    lateinit var mqttClient: Mqtt5Client
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        startForeground(0, NotificationCompat.Builder(this, notificationChannelId).build())
//        notificationManager = NotificationManagerCompat.from(this)
        notificationManager = (getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager)
        mqttClient = MqttClient.builder()
            .useMqttVersion5()
            .identifier(settingStore.thisDeviceId) //设备唯一标识
            .serverHost("10.10.34.60")
            .simpleAuth()
            .username("easy")
            .password("y0urp@ss".toByteArray(StandardCharsets.UTF_8))
            .applySimpleAuth()
            .addConnectedListener {
                Log.e("8paw9w6ccx","onconnect")
            }
            .addDisconnectedListener {
                Log.e("8paw9w6ccx","ondisconnect")
            }
            .automaticReconnectWithDefaultConfig()
            .build()

        mqttClient.toAsync().connectWith()
            .cleanStart(true)
            .keepAlive(30)
            .send()

        mqttClient.toAsync().subscribeWith()
            .topicFilter("PDU1T8Ia8InUaSEcEhRG10Y8Thr76crdQiyFK_text")
            .qos(MqttQos.AT_LEAST_ONCE)
            .callback { publish ->
                System.out.println(
                    "topic name：" + publish.getTopic() + "  topic value：" +
                            String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8)
                )
                val text = publish.payloadAsBytes.decodeToString()
                notificationManager.notify(channelId++,NotificationCompat.Builder(this,notificationChannelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setGroup("notificationChannelId")
                    .setContentText(text)
                    .build())

                updateNotificationSummary()
            }
            .send()

    }
    var showCancel = true
    var channelId = 10

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    protected fun updateNotificationSummary() {
        val numberOfNotifications = getNumberOfNotifications()
        if (numberOfNotifications > 1) { //如果数量>=2,说明有了同样组key的通知，需要归类起来
            //将通知添加/更新归类到同一组下面
            val notificationContent = "sfhsuidhfud"
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, notificationChannelId)
                    .setSmallIcon(R.mipmap.ic_launcher) //添加富样式到通知的显示样式中，如果当前系统版本不支持，那么将不起作用，依旧用原来的通知样式
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .setSummaryText(notificationContent)
                    )
                    .setAutoCancel(true)
                    .setGroup("notificationChannelId") //设置类组key，说明此条通知归属于哪一个归类
                    .setGroupSummary(true) //这句话必须和上面那句一起调用，否则不起作用
            val notification = builder.build()
            //发送通知到状态栏
            //测试发现，发送归类状态栏也是算一条通知的。所以返回值得时候，需要-1
            notificationManager.notify(
                1,
                notification
            )
        } else {
            //移除归类
            notificationManager.cancel(1)
        }
    }

    private fun getNumberOfNotifications(): Int {
        //查询当前展示的所有通知的状态列表
        val activeNotifications: Array<StatusBarNotification> = notificationManager
            .getActiveNotifications()

        //获取当前通知栏里头，NOTIFICATION_GROUP_SUMMARY_ID归类id的组别
        //因为发送分组的通知也算一条通知，所以需要-1
        for (notification in activeNotifications) {
            if (notification.id == 1) {
                //-1是因为
                return activeNotifications.size - 1
            }
        }
        return activeNotifications.size
    }

}