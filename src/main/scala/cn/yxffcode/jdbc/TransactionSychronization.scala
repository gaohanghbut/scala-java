package cn.yxffcode.jdbc

import java.sql.Connection

/**
  * @author gaohang on 6/24/16.
  */
object TransactionSychronization {
  private val threadLocal = new ThreadLocal[Connection];

  def bind(connection: Connection): Unit = {
    threadLocal.set(connection);
  }

  def get(): Connection = {
    return threadLocal.get();
  }

  def unbind(): Unit = {
    threadLocal.remove();
  }

  def isOnTransaction(connection: Connection): Boolean = {
    return threadLocal.get() == connection;
  }
}
