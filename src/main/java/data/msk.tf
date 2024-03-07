provider "aws" {
  region = "us-west-2"
}
                
resource "aws_msk_cluster" "example_cluster" {
  cluster_name = "example-msk-cluster"
  kafka_version = "2.8.0"
                
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
        enabled = true
        log_group = "msk-logs"
      }
    }
  }
}