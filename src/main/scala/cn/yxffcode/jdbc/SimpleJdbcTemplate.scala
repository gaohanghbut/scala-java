package cn.yxffcode.jdbc

import java.sql.{Connection, PreparedStatement, ResultSet, Statement}
import javax.sql.DataSource

import scala.collection.mutable.{MutableList}
import scala.collection.{Seq}

/**
  * @author gaohang on 6/24/16.
  */
class SimpleJdbcTemplate(ds: DataSource) extends JdbcTemplate {

  private val dataSource: DataSource = ds;

  override def execute[T](callback: (Connection) => T): T = {
    var connection = TransactionSychronization.get();
    if (connection == null) {
      connection = dataSource.getConnection();
    }
    try {
      return callback(connection);
    } finally {
      if (!TransactionSychronization.isOnTransaction(connection)) {
        connection.close();
      }
    }
  }

  override def execute[T](callback: (Statement) => T): T = {
    return execute((connection: Connection) => {
      val statement = connection.createStatement();
      try {
        return callback(statement);
      } finally {
        statement.close();
      }
    });
  }

  override def execute[T](sql: String, callback: (PreparedStatement) => T): T = {
    return execute((conn: Connection) => {
      val preparedStatement = conn.prepareStatement(sql);
      try {
        return callback(preparedStatement)
      } finally {
        preparedStatement.close();
      }
    });
  }

  override def query[T](sql: String, mapper: (ResultSet) => T)(params: Any*): Seq[T] = {
    return execute(sql, (ps) => {
      if (params != null && params.length != 0) {
        var idx = 1;
        for (p <- params) {
          ps.setObject(idx, p);
          idx += 1;
        }
      }
      var rs: ResultSet = null;
      try {
        var seq = new MutableList[T];
        rs = ps.executeQuery();
        while (rs.next()) {
          seq += mapper(rs);
        }
        return seq;
      } finally {
        if (rs != null) {
          rs.close();
        }
      }
    });
  }

  override def queryOne[T](sql: String, mapper: (ResultSet) => T)(params: Any*): Option[T] = {
    var elems = query(sql, mapper)(params);
    if (elems.isEmpty) {
      return Option.empty;
    }
    return Option(elems.head);
  }
}
