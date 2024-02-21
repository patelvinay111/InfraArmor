# InfraArmor
## Overview

This is a tool for reviewing Terraform infrastructure code with a focus on addressing OWASP Top Ten Concerns. It is designed to integrate directly in your IaC CI pipeline. The tool uses the OpenAI API to analyze a given input Terraform file and provides review comments with suggested changes to enhance the security of the infrastructure.

## Features

- **OWASP Top Ten Concerns:** The tool identifies potential security issues related to the OWASP Top Ten, including injection flaws, security misconfigurations, and other vulnerabilities.

- **Interactive Review:** Utilizing the OpenAI API, the tool provides interactive review comments and suggestions based on the input Terraform code.

## Usage
### Integration with CI
- Refer to the sample yml files provided in the repo for direct integration in CI
#### Gitlab
```yaml
stages:
- run_infra_armor

variables:
INFRA_ARMOR_JAR_PATH: "InfraArmor.jar"  # Update with the actual path to your InfraArmor.jar

run_infra_armor:
  stage: run_infra_armor
  script:
  - |
      # Get a list of all files ending in *.tf
      terraform_files=$(find . -type f -name "*.tf")

      # Iterate over each Terraform file and run the InfraArmor command
      for file in $terraform_files; do
        java -jar $INFRA_ARMOR_JAR_PATH "$file" -d true
      done
```

#### GitHub Workflows
```yaml
name: Run InfraArmor

on:
  push:
    paths:
      - '**/*.tf'

jobs:
  run_infra_armor:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v2

      - name: Find Terraform Files
        run: |
          terraform_files=$(find . -type f -name "*.tf")
          echo "::set-env name=TERRAFORM_FILES::$terraform_files"

      - name: Run InfraArmor
        run: |
          IFS=' ' read -ra files <<< "$TERRAFORM_FILES"
          for file in "${files[@]}"; do
            java -jar InfraArmor.jar "$file" -d true
          done
```
### Local Usage
1. **Clone the Repository:**
    ```bash
    git clone https://github.com/patelvinay111/terraform-security-review.git
    cd terraform-security-review
    ```

2. **Set up OpenAI API:**
    - Obtain an API key from OpenAI and set it as an environment variable.
      Certainly! Here's how you can set the `OPEN_API_TOKEN` environment variable on macOS, Windows, and Linux using the command line:

   - ### macOS/Linux
   
     - ```bash
       export OPEN_API_TOKEN=your-api-token
       ```
     - Replace `your-api-token` with your actual OpenAI API key.
     - To make the change permanent, add the above line to your shell profile file (e.g., `~/.bashrc`, `~/.zshrc`, or `~/.bash_profile`).
   
   - ### Windows
   
     - ```cmd
       setx OPEN_API_TOKEN your-api-token
       ```
     - Replace `your-api-token` with your actual OpenAI API key. 
     - For the change to take effect, you may need to restart any applications or command prompts that will use the environment variable.

3. **Run the Tool:**
    ```bash
    java -jar terraform-security-review.jar -f /path/to/your/terraform/file.tf
    ```

4. **Review Comments:**
    - The tool will analyze the Terraform file and provide detailed review comments, including suggested changes and considerations for addressing security concerns.

## Dependencies

- **OpenAI API:** Ensure you have an OpenAI API key and set it in the environment variable.

- **Java:** The tool is written in Java, so ensure you have Java installed on your system.

## Sample Input

```hcl
provider "aws" {
  region = "us-west-2"
}

resource "aws_msk_cluster" "example_cluster" {
  cluster_name          = "example-msk-cluster"
  kafka_version         = "2.8.0"
  number_of_broker_nodes = 3

  encryption_info {
    encryption_at_rest_kms_key_arn = "arn:aws:kms:us-west-2:123456789012:key/abcd1234-a123-456a-a12b-a123b4cd5678"
  }

  client_authentication {
    sasl {
      scram {
        enabled = true
      }
    }
  }

  logging_info {
    broker_logs {
      cloudwatch_logs {
        enabled  = true
        log_group = "msk-logs"
      }
    }
  }
}
```

## Sample Output

```json
[
  {
    "suggestion": "The region should be reviewed to ensure that it is the intended region for the cluster.",
    "recommendation": "Review and update the region to the intended region.",
    "OWASP_top_ten_concern": "Security Misconfiguration",
    "line_numbers": "5-7"
  },
  {
    "suggestion": "The cluster name should be randomized to prevent potential attackers from guessing the name and gaining unauthorized access to the cluster.",
    "recommendation": "Randomize the cluster name.",
    "OWASP_top_ten_concern": "Security Misconfiguration",
    "line_numbers": "9-10"
  },
  // ... (other suggestions)
]
```

## Options

- `-d true`: Display the output as a Markdown table.

### Sample Markdown Table Output
Review comments for: `/path/msk.tf`

| Line Numbers | OWASP Category | Suggestion | Recommendation |
|------|-----|------|------|
| 5-7 | Security Misconfiguration | The region should be reviewed to ensure that it is the intended region for the cluster. | Review and update the region to the intended region. |
| 9-10 | Security Misconfiguration | The cluster name should be randomized to prevent potential attackers from guessing the name and gaining unauthorized access to the cluster. | Randomize the cluster name. |
| 11 | Security Misconfiguration | The Kafka version should be reviewed to ensure that it is the latest and most secure version. | Update the Kafka version to the latest and most secure version. |
| 13 | Security Misconfiguration | The number of broker nodes should be reviewed to ensure that it is appropriate for the cluster. | Review and update the number of broker nodes to appropriate value. |
| 15-19 | Insecure Direct Object References | The KMS key ARN should be reviewed to ensure that it is the intended key for encryption at rest. | Review and update the KMS key ARN to the intended key for encryption at rest. |
| 21-24 | Insecure Direct Object References | The client authentication should be reviewed to ensure that only necessary authentication methods are enabled. | Review and update the client authentication to only enable necessary methods. |
| 26-31 | Security Misconfiguration | The logging configuration should be reviewed to ensure that it is appropriate for the cluster. | Review and update the logging configuration to appropriate values. |


## Usage

```bash
java -jar InfraArmor.jar <path_to_terraform_file> -d true
```

## Contributors

- [patelvinay111](https://github.com/patelvinay111)

## Contribution Guidelines

Feel free to contribute to this project by submitting issues, feature requests, or pull requests. Follow the guidelines outlined in the [CONTRIBUTING.md](CONTRIBUTING.md) file.


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.