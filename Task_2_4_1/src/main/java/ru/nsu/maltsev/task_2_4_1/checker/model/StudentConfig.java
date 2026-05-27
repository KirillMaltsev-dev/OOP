package ru.nsu.maltsev.task_2_4_1.checker.model;

public class StudentConfig {
    private String github;
    private String fullName;
    private String repositoryUrl;

    public String getGithub() {
        return github;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }
}