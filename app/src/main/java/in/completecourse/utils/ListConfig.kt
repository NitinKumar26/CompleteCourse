package `in`.completecourse.utils

import `in`.completecourse.R

object ListConfig {
    @JvmField
    val subjectIntermediate = arrayOf("भौतिक विज्ञान", "रसायन विज्ञान", "गणित", "जीवविज्ञान")
    @JvmField
    val subjectHighSchool = arrayOf("विज्ञान", "Science", "गणित", "Mathematics")
    @JvmField
    val imagesHighSchool = intArrayOf(R.drawable.ic_science, R.drawable.ic_science, R.drawable.ic_home_mathematics, R.drawable.ic_home_mathematics)
    @JvmField
    val imagesIntermediate = intArrayOf(R.drawable.ic_home_physics, R.drawable.ic_home_chemistry, R.drawable.ic_home_mathematics, R.drawable.ic_home_biology)
    val classCode = arrayOf("4", "1", "2", "3")
    val subjectCodeNinth = arrayOf("1", "16", "4", "15")
    val subjectCodeTenth = arrayOf("2", "13", "3", "14")
    val subjectCodeEleven = arrayOf("7", "9", "5", "11")
    val subjectCodeTwelve = arrayOf("8", "10", "6", "12")
}