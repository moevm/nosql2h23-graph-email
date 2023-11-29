package com.example.backend.api.dto

import com.google.gson.annotations.SerializedName

data class MailListResponse(

    @SerializedName("nodes_person")
    val nodesPerson: List<NodesPersonResponse>? = null,
    @SerializedName("nodes_letter")
    val nodesLetter: List<NodesLetterResponse>? = null,
    @SerializedName("links")
    val links: List<LinksResponse>? = null,
    @SerializedName("main_person")
    val mainPerson: MainPersonResponse? = null
)

data class NodesPersonResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("labels")
    val labels: List<String>? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("email")
    val email: String? = null
)

data class NodesLetterResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("labels")
    val labels: List<String>? = null,
    @SerializedName("from")
    val from: String? = null,
    @SerializedName("full_text")
    val fullText: String? = null,
    @SerializedName("id_chain")
    val idChain: Int? = null,
    @SerializedName("order_in_chain")
    val orderInChain: Int? = null,
    @SerializedName("subject")
    val subject: String? = null,
    @SerializedName("to")
    val to: List<String>? = null,
    @SerializedName("time_on_reply")
    val timeOnReply: String? = null
)

data class LinksResponse(
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("source")
    val source: String? = null,
    @SerializedName("target")
    val target: String? = null,
    @SerializedName("type")
    val type: String? = null
)

data class MainPersonResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("labels")
    val labels: List<String>? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("email")
    val email: String? = null
)