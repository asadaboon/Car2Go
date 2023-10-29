package com.example.car2go.common.cryptographic.config

import com.example.car2go.common.cryptographic.core.enums.EncryptionMode
import com.example.car2go.common.cryptographic.core.enums.EncryptionPadding

data class EncryptionAesWithRsaConfig(
    val rsaAlias: String,
    var aesBlockMode: EncryptionMode,
    var rsaBlockMode: EncryptionMode,
    var rsaEncryptionPadding: EncryptionPadding,
    var aesEncryptionPadding: EncryptionPadding
)