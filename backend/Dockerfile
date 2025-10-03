# Estágio 1: Build da aplicação com o Maven
# Usamos uma imagem que já vem com o JDK 17 e o Maven
FROM eclipse-temurin:17-jdk-jammy as builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos de configuração do Maven para baixar as dependências primeiro
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Baixa as dependências (isso cria uma camada em cache no Docker)
RUN ./mvnw dependency:go-offline

# Copia o resto do código-fonte da aplicação
COPY src ./src

# Executa o build, empacotando a aplicação em um arquivo .jar. Pula os testes.
RUN ./mvnw package -DskipTests

# Estágio 2: Criação da imagem final de execução
# Usamos uma imagem base do Java muito menor, apenas com o necessário para rodar
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Copia apenas o arquivo .jar gerado no estágio anterior para a imagem final
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta 8080, que é a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação quando o contêiner for executado
ENTRYPOINT ["java", "-Xmx800m", "-jar", "app.jar"]