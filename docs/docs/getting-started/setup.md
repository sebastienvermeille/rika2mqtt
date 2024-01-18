---
sidebar_position: 1
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Setup

RIKA2MQTT can be used on different platforms.

It is mainly built for Docker but some other environment are also available.

> Need another deployment platform ? Please ask :)

<Tabs
values={[
{ label: 'Docker', value: 'docker' },
{ label: 'Unraid', value: 'unraid' },
]}
>
    <TabItem value="docker">
        # Docker

        ```
        docker run \
            -e RIKA_EMAIL=<yourRikaFirenetAccount> \
            -e RIKA_PASSWORD=<yourPassword> \
            -e MQTT_HOST=127.0.0.1 \
            -e MQTT_USER=<optional> \
            -e MQTT_PASSWORD=<optional> \
            -d --name rika2mqtt cookiecodedev/rika2mqtt:{{ versions.stable }}
        ```


        Last stable version is: `2.0.0` or the tag `stable` (point always to the last stable release)


        If you prefer, you can use `latest` instead which can be unstable (not recommended if you are not a rika2mqtt contributor)
    

        To know more about these env please read the configuration page.
    </TabItem>
    <TabItem value="unraid">
        RIKA2MQTT is available on unRAID. In the community apps search for: `rika2mqtt` or click the following link:

        https://unraid.net/community/apps?q=rika2mqtt#r

![unraid-app-rika2mqtt](/assets/unraid/unraid-community-rika2mqtt.png)
    </TabItem>
</Tabs>
