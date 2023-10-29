package com.example.car2go.data.dao

import io.realm.Realm
import io.realm.RealmModel

abstract class BaseDao<Z : RealmModel> {

    open fun save(input: Z) {
        Realm.getDefaultInstance().use { it ->
            it.executeTransaction {
                it.copyToRealmOrUpdate(input)
            }
        }
    }

    open fun save(inputs: List<Z>) {
        Realm.getDefaultInstance().use { it ->
            it.executeTransaction {
                it.copyToRealmOrUpdate(inputs)
            }
        }
    }

    open fun saveAsync(input: Z) {
        Realm.getDefaultInstance().use { it ->
            it.executeTransactionAsync {
                it.copyToRealmOrUpdate(input)
            }
        }
    }
}