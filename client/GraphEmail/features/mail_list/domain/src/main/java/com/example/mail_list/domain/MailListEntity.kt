package com.example.mail_list.domain

data class MailListEntity(
    val nodesPerson: List<NodesPerson>? = null,
    val nodesLetter: List<NodesLetter>? = null,
    val links: List<Links>? = null,
    val mainPerson: MainPerson? = null
)

data class NodesPerson(

    val id: String? = null,
    val labels: List<String>? = null,
    val name: String? = null,
    val email: String? = null
)

data class NodesLetter(
    val id: String? = null,
    val labels: List<String>? = null,
    val from: String? = null,
    val fullText: String? = null,
    val idChain: Int? = null,
    val orderInChain: Int? = null,
    val subject: String? = null,
    val to: List<String>? = null,
    val timeOnReply: String? = null
)

data class Links(
    val date: String? = null,
    val source: String? = null,
    val target: String? = null,
    val type: String? = null
)

data class MainPerson(
    val id: String? = null,
    val labels: List<String>? = null,
    val name: String? = null,
    val email: String? = null
)