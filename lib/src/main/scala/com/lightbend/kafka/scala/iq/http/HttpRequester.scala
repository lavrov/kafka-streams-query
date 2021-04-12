/**
 * Copyright (C) 2018 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.kafka.scala.iq
package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import akka.http.scaladsl.model.{ HttpResponse, HttpRequest, ResponseEntity }
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.{ Unmarshal, Unmarshaller }

import akka.stream.Materializer

import scala.concurrent.{ Future, ExecutionContext}

import com.typesafe.scalalogging.LazyLogging
import services.HostStoreInfo
import java.io.IOException

/**
 * Provides a generic API over HTTP to query from a host and a store. The result is
 * returned as a Future.
 */ 
class HttpRequester(val actorSystem: ActorSystem, val mat: Materializer,
                    val executionContext: ExecutionContext) extends LazyLogging {

  private implicit val as: ActorSystem = actorSystem
  private implicit val mt: Materializer = mat
  private implicit val ec: ExecutionContext = executionContext

  private def apiRequest(path: String, host: HostStoreInfo): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(uri = s"http://${host.host}:${host.port}$path"))

  def queryFromHost[V](host: HostStoreInfo, 
    path: String)(implicit u: Unmarshaller[ResponseEntity, V]): Future[V] = {
    apiRequest(path, host).flatMap { response =>
      response.status match {
        case OK          => Unmarshal(response.entity).to[V]
         
        case BadRequest  => {
          logger.error(s"$path: incorrect path")
          Future.failed(new IOException(s"$path: incorrect path"))
        }

        case otherStatus => Unmarshal(response.entity).to[String].flatMap { entity =>
          val error = s"state fetch request failed with status code ${otherStatus} and entity $entity"
          logger.error(error)
          Future.failed(new IOException(error))
        }
      }
    }
  }
}
