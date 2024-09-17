package projects.abdulrahman.models

import kotlinx.serialization.Serializable

@Serializable
data class BlogPost(
    var title: String,

    var content: String


)