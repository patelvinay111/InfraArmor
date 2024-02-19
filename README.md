# InfraArmor
## Overview

This is a tool for reviewing Terraform infrastructure code with a focus on addressing OWASP Top Ten Concerns. It is designed to integrate directly in your IaC CI pipeline. The tool uses the OpenAI API to analyze a given input Terraform file and provides review comments with suggested changes to enhance the security of the infrastructure.

## Features

- **OWASP Top Ten Concerns:** The tool identifies potential security issues related to the OWASP Top Ten, including injection flaws, security misconfigurations, and other vulnerabilities.

- **Interactive Review:** Utilizing the OpenAI API, the tool provides interactive review comments and suggestions based on the input Terraform code.

## Usage

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