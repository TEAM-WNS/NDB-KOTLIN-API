package com.ndb.engine

import com.ndb.engine.options.StrictOptions

class NDB(val uri: String, val option: StrictOptions = StrictOptions.NONSTRICT) {}