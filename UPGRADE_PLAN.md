# Plano de Atualização WM-Logistics

## 📊 Análise do Estado Atual

### Dependências Principais (Versões Atuais)

| Dependência | Versão Atual | Lançamento | Anos Defasados |
|-------------|--------------|------------|----------------|
| **Spring Framework** | 4.2.0.RELEASE | Set/2015 | ~10 anos |
| **Jackson** | 2.5.3 | Mar/2015 | ~10 anos |
| **Neo4j OGM** | 1.1.1 | 2015 | ~10 anos |
| **Neo4j** | 2.0.3 | Dez/2013 | ~12 anos |
| **Hibernate Validator** | 5.1.3.Final | 2014 | ~11 anos |
| **JUnit** | 4.10 | 2012 | ~13 anos |
| **REST Assured** | 2.5.0 | 2015 | ~10 anos |
| **Servlet API** | 3.1.0 | 2013 | ~12 anos |

### 🚨 Riscos Identificados

#### Segurança
- ⚠️ **CRÍTICO**: Spring 4.2.0 tem múltiplas CVEs conhecidas
- ⚠️ **CRÍTICO**: Jackson 2.5.3 vulnerável a desserialização remota (CVE-2017-7525, CVE-2017-15095)
- ⚠️ **ALTO**: Neo4j 2.0.3 extremamente desatualizado, sem suporte
- ⚠️ **MÉDIO**: Hibernate Validator 5.1.3 com vulnerabilidades conhecidas

#### Compatibilidade
- ❌ Java 8 (JDK 1.8) está em fim de vida útil
- ❌ Neo4j 2.x não é compatível com versões modernas
- ❌ Spring 4.x não suporta recursos modernos do Java

#### Manutenção
- ❌ Sem patches de segurança
- ❌ Sem suporte da comunidade
- ❌ Dificulta contratação de desenvolvedores

---

## 🎯 Estratégia de Atualização

### Abordagem: **Migração Incremental em Fases**

**Princípios:**
1. ✅ Uma mudança major por vez
2. ✅ Testes completos após cada fase
3. ✅ Manter funcionalidade durante migração
4. ✅ Possibilidade de rollback em qualquer ponto

---

## 📋 Plano de Execução (5 Fases)

### **Fase 0: Preparação e Baseline** (1-2 dias)

#### Objetivos:
- Estabelecer ambiente de testes
- Criar suite de testes de regressão
- Documentar comportamento atual
- Criar backup completo

#### Tarefas:
- [ ] Criar branch `migration/preparation`
- [ ] Executar todos os testes atuais e documentar resultados
- [ ] Criar testes de integração end-to-end se não existirem
- [ ] Documentar todos os endpoints e comportamentos esperados
- [ ] Configurar CI/CD pipeline para testes automatizados
- [ ] Fazer backup completo do banco Neo4j
- [ ] Criar snapshot da aplicação atual funcionando

#### Entregáveis:
- ✅ Suite de testes completa executando
- ✅ Documentação de endpoints e comportamentos
- ✅ Backup completo
- ✅ Pipeline CI/CD configurado

---

### **Fase 1: Atualização Java e Build Tools** (2-3 dias)

#### Objetivo:
Atualizar para Java 11 LTS (ou 17 LTS) mantendo compatibilidade

#### Mudanças:

```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

#### Atualizações:
- **Java**: 8 → 11 (ou 17)
- **Maven Compiler Plugin**: → 3.11.0
- **Maven War Plugin**: → 3.4.0
- **Wildfly Maven Plugin**: 1.1.0.Alpha1 → 5.0.0.Final

#### Tarefas:
- [ ] Atualizar `pom.xml` com versão Java
- [ ] Atualizar plugins Maven
- [ ] Compilar projeto
- [ ] Corrigir warnings de deprecação
- [ ] Executar testes
- [ ] Validar WAR gerado

#### Riscos:
- 🔴 **Baixo**: Java 11 é majoritariamente compatível com Java 8

#### Rollback:
- Reverter mudanças no `pom.xml`
- Voltar para JDK 8

---

### **Fase 2: Atualização Spring Framework 4.x → 5.x** (3-5 dias)

#### Objetivo:
Atualizar Spring para versão 5.3.x (última da série 5.x, LTS)

#### Mudanças:

```xml
<properties>
    <springframework.version>5.3.31</springframework.version> <!-- Latest 5.x LTS -->
</properties>
```

#### Dependências Adicionais:
```xml
<!-- Necessário para Spring 5 -->
<dependency>
    <groupId>javax.annotation</groupId>
    <artifactId>javax.annotation-api</artifactId>
    <version>1.3.2</version>
</dependency>
```

#### Mudanças de Código Necessárias:

1. **Substituir imports deprecados:**
```java
// Antes
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// Depois (melhor prática Spring 5)
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
```

2. **Atualizar configuração XML** (se aplicável)

#### Tarefas:
- [ ] Atualizar versão Spring no `pom.xml`
- [ ] Adicionar dependências faltantes (javax.annotation)
- [ ] Compilar e corrigir erros de compilação
- [ ] Atualizar configurações XML se necessário
- [ ] Executar todos os testes unitários
- [ ] Executar testes de integração
- [ ] Testar manualmente todos os endpoints

#### Riscos:
- 🟡 **Médio**: Spring 5 tem mudanças de API, mas mantém compatibilidade

#### Pontos de Atenção:
- Configurações de servlet podem precisar ajustes
- Validações podem ter comportamento ligeiramente diferente

#### Rollback:
- Reverter versão do Spring para 4.2.0
- Remover dependências adicionadas

---

### **Fase 3: Atualização Jackson e Bibliotecas Auxiliares** (2-3 dias)

#### Objetivo:
Corrigir vulnerabilidades críticas de segurança

#### Mudanças:

```xml
<properties>
    <jackson.version>2.15.3</jackson.version> <!-- Latest 2.x -->
</properties>

<dependencies>
    <!-- Hibernate Validator -->
    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>6.2.5.Final</version>
    </dependency>

    <!-- Servlet API -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>

    <!-- REST Assured -->
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.3.2</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit Vintage para manter compatibilidade JUnit 4 -->
    <dependency>
        <groupId>org.junit.vintage</groupId>
        <artifactId>junit-vintage-engine</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### Tarefas:
- [ ] Atualizar Jackson
- [ ] Atualizar Hibernate Validator
- [ ] Atualizar REST Assured (mudar groupId)
- [ ] Migrar testes de JUnit 4 → 5 (incremental)
- [ ] Executar testes
- [ ] Verificar serialização JSON

#### Riscos:
- 🟡 **Médio**: Jackson pode ter mudanças sutis em serialização
- 🟢 **Baixo**: Hibernate Validator mantém compatibilidade

#### Pontos de Atenção:
- Testar JSON serialization/deserialization cuidadosamente
- Validar que anotações de validação funcionam corretamente

#### Rollback:
- Reverter versões das bibliotecas

---

### **Fase 4: Migração Neo4j (CRÍTICA)** (5-7 dias)

#### Objetivo:
Migrar Neo4j 2.x → 4.x ou 5.x (mudança mais complexa)

#### ⚠️ ATENÇÃO: Esta é a fase mais arriscada!

#### Opções de Migração:

##### **Opção A: Neo4j 4.x (Recomendado para primeira fase)**
- Suporte até 2024
- Mais estável
- Migração menos complexa

##### **Opção B: Neo4j 5.x (Futuro)**
- Suporte de longo prazo
- Requer mais mudanças

#### Mudanças para Neo4j 4.x:

```xml
<properties>
    <neo4j.version>4.4.37</neo4j.version>
    <neo4j-ogm.version>3.2.41</neo4j-ogm.version>
</properties>

<dependencies>
    <!-- Neo4j OGM -->
    <dependency>
        <groupId>org.neo4j</groupId>
        <artifactId>neo4j-ogm-core</artifactId>
        <version>${neo4j-ogm.version}</version>
    </dependency>

    <dependency>
        <groupId>org.neo4j</groupId>
        <artifactId>neo4j-ogm-bolt-driver</artifactId>
        <version>${neo4j-ogm.version}</version>
    </dependency>

    <!-- Neo4j Driver (substitui JDBC) -->
    <dependency>
        <groupId>org.neo4j.driver</groupId>
        <artifactId>neo4j-java-driver</artifactId>
        <version>4.4.12</version>
    </dependency>
</dependencies>
```

#### Mudanças de Código Necessárias:

1. **Atualizar Connection Factory:**

```java
// ANTES (Neo4j 2.x)
// Neo4jConnectionFactory.java
public class Neo4jConnectionFactory {
    private static Neo4jConnectionFactory instance;
    // ... código antigo
}

// DEPOIS (Neo4j 4.x)
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jConnectionFactory {
    private static Driver driver;

    public static Driver getDriver() {
        if (driver == null) {
            driver = GraphDatabase.driver(
                "bolt://localhost:7687",
                AuthTokens.basic("neo4j", "password")
            );
        }
        return driver;
    }
}
```

2. **Atualizar Session Factory:**

```java
// DEPOIS (Neo4j 4.x OGM)
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

public class Neo4jSessionFactory {
    private static SessionFactory sessionFactory;

    public static SessionFactory getInstance() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration.Builder()
                .uri("bolt://localhost:7687")
                .credentials("neo4j", "password")
                .build();

            sessionFactory = new SessionFactory(configuration,
                "br.com.walmart.freight.entities.graph");
        }
        return sessionFactory;
    }
}
```

3. **Atualizar Queries Cypher:**

Neo4j 4.x mudou algumas funções:
```cypher
-- ANTES
MATCH (n) WHERE has(n.property)

-- DEPOIS
MATCH (n) WHERE n.property IS NOT NULL
```

#### Migração de Dados:

1. **Backup Neo4j 2.x:**
```bash
neo4j-admin dump --database=graph.db --to=/backup/neo4j-2.dump
```

2. **Instalar Neo4j 4.x em paralelo** (porta diferente)

3. **Migrar dados:**
```bash
# Exportar de Neo4j 2.x
neo4j-shell -c "MATCH (n) RETURN n" > export.cypher

# Importar para Neo4j 4.x usando neo4j-admin
```

#### Tarefas:
- [ ] Instalar Neo4j 4.x em ambiente de teste
- [ ] Fazer backup completo do Neo4j 2.x
- [ ] Migrar dados para Neo4j 4.x
- [ ] Atualizar dependências no pom.xml
- [ ] Refatorar código de conexão
- [ ] Atualizar queries Cypher (se necessário)
- [ ] Testar todas as operações CRUD
- [ ] Testar cálculo de shortest path
- [ ] Validar performance
- [ ] Testar integração completa

#### Riscos:
- 🔴 **ALTO**: Neo4j mudou APIs significativamente
- 🔴 **ALTO**: Migração de dados pode ter problemas
- 🔴 **ALTO**: Queries Cypher podem precisar ajustes

#### Estratégia de Mitigação:
1. Manter Neo4j 2.x e 4.x rodando em paralelo
2. Testar exaustivamente em ambiente isolado
3. Validar todos os dados após migração
4. Ter plano de rollback claro

#### Rollback:
- Restaurar backup do Neo4j 2.x
- Reverter código para usar APIs antigas
- Reverter dependências do pom.xml

---

### **Fase 5: Spring Boot Migration (OPCIONAL - Futuro)** (1-2 semanas)

#### Objetivo:
Modernizar completamente para Spring Boot 3.x

#### Por que considerar:
- ✅ Configuração simplificada
- ✅ Embedded server (não precisa Wildfly)
- ✅ Melhores práticas modernas
- ✅ Melhor suporte e documentação
- ✅ Métricas e health checks prontos

#### Mudanças Principais:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-neo4j</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### Mudanças de Código:

1. **Criar Application class:**
```java
@SpringBootApplication
@EnableNeo4jRepositories("br.com.walmart.freight.repositories")
public class WmLogisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(WmLogisticsApplication.class, args);
    }
}
```

2. **Migrar de XML para Java Config:**
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Configurações de converters
    }
}
```

3. **Criar application.properties:**
```properties
spring.neo4j.uri=bolt://localhost:7687
spring.neo4j.authentication.username=neo4j
spring.neo4j.authentication.password=password

server.port=8080
server.servlet.context-path=/wm-logistics
```

#### Benefícios:
- ✅ Deployment mais simples (JAR executável)
- ✅ Sem necessidade de Wildfly
- ✅ DevTools para hot reload
- ✅ Actuator para monitoring
- ✅ Configuração mais clara

#### Riscos:
- 🔴 **ALTO**: Requer refatoração significativa
- 🟡 **Médio**: Mudança de paradigma de deployment

---

## 🧪 Estratégia de Testes

### Testes Automatizados (Cada Fase)

#### 1. **Testes Unitários**
```bash
mvn clean test
```
- ✅ Todos os testes unitários devem passar
- ✅ Coverage deve se manter ou melhorar

#### 2. **Testes de Integração**
```bash
mvn clean verify -Pitest
```
- ✅ Todos os endpoints REST funcionando
- ✅ Validações funcionando
- ✅ Erros retornando corretamente

#### 3. **Testes de Regressão**

Criar script de testes manuais:

```bash
#!/bin/bash
# test-regression.sh

BASE_URL="http://localhost:8080/wm-logistics"

# 1. Criar mapa de rotas
echo "Testing POST /maps..."
curl -X POST $BASE_URL/maps \
  -H "Content-Type: application/json" \
  -d '{"name":"SP","routes":[{"from":"A","to":"B","distance":10},{"from":"B","to":"D","distance":15}]}'

# 2. Calcular frete
echo "Testing POST /freights..."
curl -X POST $BASE_URL/freights \
  -H "Content-Type: application/json" \
  -d '{"map":"SP","from":"A","to":"D","autonomy":10,"price":2.5}'

# 3. Validar erro (entrada inválida)
echo "Testing validation errors..."
curl -X POST $BASE_URL/freights \
  -H "Content-Type: application/json" \
  -d '{"map":"","from":"","to":"","autonomy":null,"price":null}'

echo "Regression tests completed!"
```

#### 4. **Testes de Performance**

```bash
# Usando Apache Bench
ab -n 1000 -c 10 -T "application/json" \
   -p freight-request.json \
   http://localhost:8080/wm-logistics/freights
```

Validar que performance não degrada significativamente.

#### 5. **Testes de Segurança**

```bash
# Scan de vulnerabilidades
mvn dependency-check:check

# OWASP ZAP scan
zap-cli quick-scan http://localhost:8080/wm-logistics
```

---

## 📦 Plano de Rollback

### Para Cada Fase:

#### 1. **Git Strategy**
```bash
# Criar branch para cada fase
git checkout -b migration/phase-1-java
# ... fazer mudanças
git commit -m "Phase 1: Java 11 upgrade"

# Se precisar voltar:
git checkout main
```

#### 2. **Database Backup**
```bash
# Antes de cada fase que mexe com Neo4j:
neo4j-admin dump --database=graph.db --to=/backup/pre-phase-4.dump

# Restaurar se necessário:
neo4j-admin load --from=/backup/pre-phase-4.dump --database=graph.db --force
```

#### 3. **Application Backup**
```bash
# Backup do WAR atual
cp target/wm-logistics.war backup/wm-logistics-pre-upgrade.war

# Restaurar se necessário:
cp backup/wm-logistics-pre-upgrade.war $WILDFLY_HOME/standalone/deployments/
```

---

## 📊 Métricas de Sucesso

### Para Cada Fase:

#### ✅ Critérios de Aceitação:
- [ ] 100% dos testes unitários passando
- [ ] 100% dos testes de integração passando
- [ ] Todos os endpoints funcionais testados manualmente
- [ ] Performance igual ou melhor que versão anterior
- [ ] Sem novas vulnerabilidades de segurança
- [ ] Build rodando sem warnings críticos

#### 📈 KPIs:
- **Tempo de build**: < 2 minutos
- **Cobertura de testes**: ≥ baseline atual
- **Tempo de resposta API**: ± 10% do baseline
- **Uso de memória**: ± 20% do baseline

---

## 🗓️ Timeline Estimado

| Fase | Duração | Risco | Esforço |
|------|---------|-------|---------|
| **Fase 0**: Preparação | 1-2 dias | 🟢 Baixo | Médio |
| **Fase 1**: Java + Maven | 2-3 dias | 🟢 Baixo | Baixo |
| **Fase 2**: Spring 5.x | 3-5 dias | 🟡 Médio | Médio |
| **Fase 3**: Jackson + Libs | 2-3 dias | 🟡 Médio | Baixo |
| **Fase 4**: Neo4j 4.x | 5-7 dias | 🔴 Alto | Alto |
| **Fase 5**: Spring Boot (Opcional) | 1-2 semanas | 🔴 Alto | Alto |

**Total (sem Fase 5)**: ~3-4 semanas
**Total (com Fase 5)**: ~5-7 semanas

---

## 🎯 Recomendação Final

### Abordagem Recomendada:

#### **Curto Prazo (Crítico - 3-4 semanas):**
Executar Fases 1-4 para eliminar riscos de segurança e garantir suporte.

**Prioridades:**
1. 🔥 **Fase 3** (Jackson/Security): Resolver CVEs críticas PRIMEIRO
2. ⚡ **Fase 1** (Java): Base para outras atualizações
3. 🔄 **Fase 2** (Spring): Modernização do framework
4. 💾 **Fase 4** (Neo4j): Complexa mas necessária

#### **Médio Prazo (Opcional - 2-3 meses depois):**
Avaliar migração para Spring Boot (Fase 5) após estabilização.

---

## 📝 Próximos Passos Imediatos

1. **Revisar este plano** com a equipe
2. **Aprovar orçamento** de tempo e recursos
3. **Criar ambiente** de desenvolvimento isolado
4. **Executar Fase 0** (Preparação)
5. **Começar Fase 1** (Java update)

---

## 📞 Suporte e Recursos

### Documentação de Referência:
- [Spring 4 → 5 Migration Guide](https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-5.x)
- [Neo4j 2.x → 4.x Migration](https://neo4j.com/docs/upgrade-migration-guide/current/)
- [Jackson 2.5 → 2.15 Release Notes](https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.15)

### Comunidade:
- Stack Overflow
- Spring Community Forums
- Neo4j Community Forum

---

**Documento criado em**: 2025-12-10
**Versão**: 1.0
**Autor**: AI Assistant (Claude)
**Status**: 📋 Aguardando Aprovação
