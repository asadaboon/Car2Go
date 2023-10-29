package com.example.car2go.common.cryptographic.builder

import android.content.Context
import android.os.Build
import com.example.car2go.common.cryptographic.IEncryptionAesWithRsa
import com.example.car2go.common.cryptographic.aesWithrsa.AesWithRsaEncryptionM
import com.example.car2go.common.cryptographic.config.EncryptionAesWithRsaConfig
import com.example.car2go.common.cryptographic.core.enums.EncryptionMode
import com.example.car2go.common.cryptographic.core.enums.EncryptionPadding
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.cert.CertificateException
import javax.crypto.NoSuchPaddingException

class EncryptionAesWithRsaBuilder(
    val rsaAlias: String,val context: Context
) {

    companion object {
        private val BLOCK_MODE_DEFAULT__AES = EncryptionMode.CBC
        private val ENCRYPTION_PADDING_DEFAULT__AES = EncryptionPadding.PKCS7
        private val BLOCK_MODE_DEFAULT__RSA = EncryptionMode.ECB
        private val ENCRYPTION_PADDING_DEFAULT__RSA = EncryptionPadding.RSA_PKCS1
    }

    var config: EncryptionAesWithRsaConfig = EncryptionAesWithRsaConfig(
        rsaAlias = rsaAlias,
        rsaBlockMode = BLOCK_MODE_DEFAULT__RSA, aesBlockMode = BLOCK_MODE_DEFAULT__AES,
        rsaEncryptionPadding = ENCRYPTION_PADDING_DEFAULT__RSA, aesEncryptionPadding = ENCRYPTION_PADDING_DEFAULT__AES
    )

    @Throws(
        CertificateException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class,
        IOException::class,
        NoSuchPaddingException::class
    )
    fun build(): IEncryptionAesWithRsa {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return AesWithRsaEncryptionM(
                config = config,
                context = context
            )
        } else {
            throw NoSuchAlgorithmException("AES is support only above API Lv23.")
        }
    }
}