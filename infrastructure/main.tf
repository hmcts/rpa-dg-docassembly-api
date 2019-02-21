provider "azurerm" {
  version = "1.21"
}

locals {
  app_full_name = "${var.product}-${var.component}"
  ase_name = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  shared_vault_name = "${var.shared_product_name}-${local.local_env}"

  previewVaultName = "${local.app_full_name}-aat"
  nonPreviewVaultName = "${local.app_full_name}-${var.env}"
  vaultName = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName}"

  nonPreviewVaultUri = "${module.rpa-dg-docassembly-api-vault.key_vault_uri}"
  previewVaultUri = "https://cet-online-app-aat.vault.azure.net/"
  vaultUri = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultUri : local.nonPreviewVaultUri}"

  previewEnv= "aat"
  nonPreviewEnv = "${var.env}"
  localenv = "${(var.env == "preview" || var.env == "spreview") ? local.previewEnv : local.nonPreviewEnv}"

  s2s_vault_url = "https://s2s-${local.local_env}.vault.azure.net/"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.ase_name}"
  s2s_url = "http://${var.s2s_url}-${local.local_env}.service.${local.local_ase}.internal"
}

module "app" {
  source = "git@github.com:hmcts/cnp-module-webapp?ref=master"
  product = "${local.app_full_name}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"
  subscription = "${var.subscription}"
  capacity     = "${var.capacity}"
  is_frontend = false
  additional_host_name = "${local.app_full_name}-${var.env}.service.${var.env}.platform.hmcts.net"
  https_only="false"
  common_tags  = "${var.common_tags}"
  asp_rg = "${var.shared_product_name}-${var.env}"
  asp_name = "${var.shared_product_name}-${var.env}"

  app_settings = {
    # idam
    IDAM_API_URL = "${var.idam_api_url}"
    S2S_URL = "http://${var.s2s_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"
    S2S_KEY = "${data.azurerm_key_vault_secret.s2s_key.value}"
    S2S_NAMES_WHITELIST = "${var.s2s_names_whitelist}"

    # logging vars & healthcheck
    REFORM_SERVICE_NAME = "${local.app_full_name}"
    REFORM_TEAM = "${var.team_name}"
    REFORM_SERVICE_TYPE = "${var.app_language}"
    REFORM_ENVIRONMENT = "${var.env}"

    PACKAGES_NAME = "${local.app_full_name}"
    PACKAGES_PROJECT = "${var.team_name}"
    PACKAGES_ENVIRONMENT = "${var.env}"

    ROOT_APPENDER = "${var.root_appender}"
    JSON_CONSOLE_PRETTY_PRINT = "${var.json_console_pretty_print}"
    LOG_OUTPUT = "${var.log_output}"

    # addtional log
    ROOT_LOGGING_LEVEL = "${var.root_logging_level}"
    LOG_LEVEL_SPRING_WEB = "${var.log_level_spring_web}"
    LOG_LEVEL_DM = "${var.log_level_dm}"
    SHOW_SQL = "${var.show_sql}"

    ENDPOINTS_HEALTH_SENSITIVE = "${var.endpoints_health_sensitive}"
    ENDPOINTS_INFO_SENSITIVE = "${var.endpoints_info_sensitive}"

    S2S_NAMES_WHITELIST = "${var.s2s_names_whitelist}"
    CASE_WORKER_ROLES = "${var.case_worker_roles}"

    # Toggles
    ENABLE_IDAM_HEALTH_CHECK = "${var.enable_idam_healthcheck}"
    ENABLE_S2S_HEALTH_CHECK = "${var.enable_s2s_healthcheck}"
  }
}

data "azurerm_key_vault" "shared_key_vault" {
  name = "${local.shared_vault_name}"
  resource_group_name = "${local.shared_vault_name}"
}

data "azurerm_key_vault_secret" "s2s_key" {
  name      = "microservicekey-dg-docassembly-api"
  vault_uri = "https://s2s-${local.localenv}.vault.azure.net/"
}

module "rpa-dg-docassembly-api-vault" {
  source              = "git@github.com:hmcts/moj-module-key-vault?ref=master"
  name                = "${local.vaultName}"
  product             = "${var.product}"
  env                 = "${var.env}"
  tenant_id           = "${var.tenant_id}"
  object_id           = "${var.jenkins_AAD_objectId}"
  resource_group_name = "${module.app.resource_group_name}"
  product_group_object_id = "ffb5f9a3-b686-4325-a26e-746db5279a42"
}


resource "azurerm_key_vault_secret" "S2S_KEY" {
  name = "${data.azurerm_key_vault_secret.s2s_key.name}"
  value = "${data.azurerm_key_vault_secret.s2s_key.value}"
  vault_uri = "${module.rpa-dg-docassembly-api-vault.key_vault_uri}"
}