package cn.yxffcode.dao.impl

import javax.annotation.Resource

import cn.yxffcode.dao.UserDao
import cn.yxffcode.model.User
import org.mybatis.scala.mapping.Binding._
import org.mybatis.scala.mapping.{Insert, ResultMap, XSQL, _}
import org.mybatis.scala.session._
import org.springframework.stereotype.Repository

/**
  * Created by gaohang on 5/31/16.
  */
@Repository
class UserDaoImpl extends UserDao {

  @Resource
  private var session: Session = _

  private val insertUser = new Insert[User]() {
    override def xsql: XSQL =
      <xsql>
        INSERT INTO user(name, username, password)
        VALUES (
        {"name" ?}
        ,
        {"username" ?}
        ,
        {"password" ?}
        )
      </xsql>
  }

  private val userResultMap = new ResultMap[User] {
    id(property = "id", column = "pid")
    result(property = "name", column = "full_name")
  }

  private val selectUser = new SelectOneBy[Int, User]() {
    override def xsql: XSQL = <xsql>
      SELECT id, name FROM user WHERE id =
      {"id" ?}
    </xsql>

    override var resultMap: ResultMap[User] = userResultMap
  }

  /**
    * select a user specified by id
    *
    * @param id user id
    * @return user
    */
  override def findById(id: Int): Option[User] = {
    return selectUser(id)(session)
  }

  /**
    * insert a user into the db
    *
    * @param user
    */
  override def insert(user: User): Unit = {
    insertUser(user)(session)
  }
}
