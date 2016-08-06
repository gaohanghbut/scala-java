package cn.yxffcode.jdbc

import java.sql.{Connection, PreparedStatement, ResultSet, Statement}
import scala.collection.{Seq}

/**
  * @author gaohang on 6/24/16.
  */
trait JdbcTemplate {

  def execute[T](callback: Connection => T): T;

  def execute[T](callback: Statement => T): T;

  def execute[T](sql: String, callback: PreparedStatement => T): T;

  def query[T](sql: String, mapper: ResultSet => T)(params: Any*): Seq[T];

  def queryOne[T](sql: String, mapper: ResultSet => T)(params: Any*): Option[T];
}
