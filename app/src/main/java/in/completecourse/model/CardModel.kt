package `in`.completecourse.model

class CardModel {
    var name: String? = null
    var thumbnail = 0
    var category_link: String? = null
    var companyPhoto: String? = null

    constructor() {}
    constructor(image: Int, name: String?) {
        thumbnail = image
        this.name = name
    }

}