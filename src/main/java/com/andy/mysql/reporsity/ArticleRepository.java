package com.andy.mysql.reporsity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.andy.lucene.entity.Article;
/**
 * 方法名的约定：
 *      findBy: 查询
 *          对象中的属性名称（首字母大写）：查询条件
 *          CustName
 *          *默认情况：使用 = 进行匹配
 *
 *      findByCustName  --  根据客户名称查询
 * 在springdatajpa 运行阶段
 *      会根据方法名称进行解析 findBy from xxx(实体类) where xxx(属性) =
 *
 *      1.findBy + 属性名称 精准匹配
 *      2.findBy + 属性名称 +“查询方式(Like | isnull)”  模糊匹配
 *          findByCustNameLike
 *      3.多条件查询
 *      findBy + 属性名称 +“查询方式"+"多条件的链接符（and | or）"+属性名+"查询方式"
 *
 *
 * 不用写jpql语句
 */

/**
 * 1.findBy + 属性名称 精准匹配
 */
public interface ArticleRepository extends JpaRepository<Article,Integer> {

//	@Query("select u from article u where id=?1")
//	public Optional<Article> findById(Integer id);
	
	@Query(value = "select u from Article u where title like ?1")
    public List<Article> findByTitleLike(String title);
	
	/**
     * 无参数查询
     * @return
     */
//    @Query("select u from article u where id =(select max(id) from article)")
//    Article getMaxId();

}
