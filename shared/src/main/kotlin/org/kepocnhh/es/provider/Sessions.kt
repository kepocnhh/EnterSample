package org.kepocnhh.es.provider

import org.kepocnhh.es.entity.EnterSalt
import org.kepocnhh.es.entity.EnterState

class Sessions(
    var privateKey: ByteArray?,
    var enterSalt: EnterSalt?,
    var enterState: EnterState?,
)
