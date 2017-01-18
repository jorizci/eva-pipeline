/*
 * Copyright 2016-2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.pipeline.t2d.io.writers;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.util.Assert;
import uk.ac.ebi.eva.pipeline.t2d.utils.JpaUpdateQueryGenerator;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * This writer uses a {@link JpaUpdateQueryGenerator} to execute update statements from type {@param T} in a Jpa
 * datasource.
 *
 * @param <T>
 */
public class JpaQueryWriter<T> implements ItemWriter<T>, InitializingBean {


    private final EntityManagerFactory entityManagerFactory;
    private final JpaUpdateQueryGenerator<T> jpaQuery;

    public JpaQueryWriter(EntityManagerFactory entityManagerFactory, JpaUpdateQueryGenerator<T> jpaQuery) {
        this.entityManagerFactory = entityManagerFactory;
        this.jpaQuery = jpaQuery;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        EntityManager entityManager = getEntityManager();
        doWrite(entityManager, items);
        entityManager.flush();
    }

    private EntityManager getEntityManager() {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
        }
        return entityManager;
    }

    private void doWrite(EntityManager entityManager, List<? extends T> items) {
        for(T item: items){
            jpaQuery.executeQuery(entityManager,item);
        }
    }

    @Override
    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(entityManagerFactory, "An EntityManagerFactory is required");
    }
}
