package com.ssuspot.sns.support.helper

import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Table
import jakarta.persistence.metamodel.EntityType

@Component
@ActiveProfiles("test")
class DatabaseCleanup : InitializingBean {
    
    @PersistenceContext
    private lateinit var entityManager: EntityManager
    
    private lateinit var tableNames: List<String>
    
    override fun afterPropertiesSet() {
        val entities: Set<EntityType<*>> = entityManager.metamodel.entities
        tableNames = entities
            .filter { e ->
                e.javaType.getAnnotation(Entity::class.java) != null
            }
            .map { e ->
                val entityClass = e.javaType
                val tableAnnotation = entityClass.getAnnotation(Table::class.java)
                tableAnnotation?.name ?: camelToSnake(entityClass.simpleName)
            }
    }
    
    @Transactional
    fun execute() {
        entityManager.flush()
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
        
        for (tableName in tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
            entityManager.createNativeQuery("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1").executeUpdate()
        }
        
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
    }
    
    private fun camelToSnake(camelCase: String): String {
        return camelCase.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
    }
}