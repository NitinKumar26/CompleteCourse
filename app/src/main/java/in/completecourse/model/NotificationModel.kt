package `in`.completecourse.model

class NotificationModel {
    private var mHeading: String? = null
    private var mSubHeading: String? = null
    var url: String? = null
    var serial: String? = null
    fun getmHeading(): String? {
        return mHeading
    }

    fun getmSubHeading(): String? {
        return mSubHeading
    }

    fun setmHeading(mHeading: String?) {
        this.mHeading = mHeading
    }

    fun setmSubHeading(mSubHeading: String?) {
        this.mSubHeading = mSubHeading
    }

    constructor() {}

    constructor(heading: String?, subheading: String?, url: String?) {
        mHeading = heading
        mSubHeading = subheading
        this.url = url
    }
}