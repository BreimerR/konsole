FROM jfrog/artifactory-oss:latest

# Set environment variables (adjust as needed)
ENV ARTIFACTORY_ADMIN_PASSWORD=password
ENV ARTIFACTORY_URL=http://localhost:8080

# Create Artifactory home directory (optional, persistent storage recommended)
WORKDIR /opt/artifactory

# Expose Artifactory port
EXPOSE 8080

# Run Artifactory server
CMD ["artifactory.server.Startup"]
