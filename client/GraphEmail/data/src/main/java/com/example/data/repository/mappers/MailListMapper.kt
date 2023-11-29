package com.example.data.repository.mappers

import com.example.backend.api.dto.LinksResponse
import com.example.backend.api.dto.MailListResponse
import com.example.backend.api.dto.MainPersonResponse
import com.example.backend.api.dto.NodesLetterResponse
import com.example.backend.api.dto.NodesPersonResponse
import com.example.mail_list.domain.Links
import com.example.mail_list.domain.MailListEntity
import com.example.mail_list.domain.MainPerson
import com.example.mail_list.domain.NodesLetter
import com.example.mail_list.domain.NodesPerson

fun MailListResponse.asDomain() : MailListEntity = MailListEntity(
    nodesPerson = nodesPerson?.map { it.asDomain() },
    nodesLetter = nodesLetter?.map { it.asDomain() },
    links = links?.map { it.asDomain() },
    mainPerson = mainPerson?.asDomain()
)

fun NodesPersonResponse.asDomain() : NodesPerson = NodesPerson(
    id = id,
    labels = labels,
    name = name,
    email = email
)

fun NodesLetterResponse.asDomain() : NodesLetter = NodesLetter(
    id = id,
    labels = labels,
    from = from,
    fullText = fullText,
    idChain = idChain,
    orderInChain = orderInChain,
    subject = subject,
    to = to,
    timeOnReply = timeOnReply
)

fun LinksResponse.asDomain() : Links = Links(
    date = date,
    source = source,
    target = target,
    type = type
)

fun MainPersonResponse.asDomain() : MainPerson = MainPerson(
    id = id,
    labels = labels,
    name = name,
    email = email
)