package com.example.car2go.common.cryptographic.config

import com.example.car2go.common.cryptographic.core.enums.CipherAlgorithm
import com.example.car2go.common.cryptographic.core.enums.EncryptionMode
import com.example.car2go.common.cryptographic.core.enums.EncryptionPadding

data class EncryptionConfig(
    var cipherAlgorithm: CipherAlgorithm,
    val alias: String,
    var blockMode: EncryptionMode,
    var encryptionPadding: EncryptionPadding
)