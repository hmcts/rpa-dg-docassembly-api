provider "azurerm" {
  version = "1.22.1"
}

locals {
  app_full_name = "${var.product}-${var.component}"
  ase_name = "core-compute-${var.env}"
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

  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.ase_name}"
  s2s_base_uri = "http://${var.s2s_name}-${local.local_env}.service.${local.local_ase}.internal"
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
  asp_name = "${var.shared_product_name}-dg-${var.env}"

  app_settings = {
    # idam
    IDAM_API_BASE_URI = "${var.idam_api_base_uri}"
    S2S_BASE_URI = "http://${var.s2s_name}-${local.local_env}.service.core-compute-${local.local_env}.internal"
    S2S_KEY = "${data.azurerm_key_vault_secret.s2s_key.value}"

    # logging vars & healthcheck
    REFORM_SERVICE_NAME = "${local.app_full_name}"
    REFORM_TEAM = "${var.team_name}"
    REFORM_SERVICE_TYPE = "${var.app_language}"
    REFORM_ENVIRONMENT = "${var.env}"

    PACKAGES_NAME = "${local.app_full_name}"
    PACKAGES_PROJECT = "${var.team_name}"
    PACKAGES_ENVIRONMENT = "${var.env}"

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

    ENABLE_FORM_DEFINITION_ENDPOINT="${var.enable_form_definition_endpoint}"
    ENABLE_TEMPLATE_RENDITION_ENDPOINT="${var.enable_template_rendition_endpoint}"

    DM_STORE_APP_URL = "http://${var.dm_store_app_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"

    DOCMOSIS_ENDPOINT = "${var.docmosis_uri}"
    DOCMOSIS_ACCESS_KEY = "${data.azurerm_key_vault_secret.docmosis_access_key.value}"

    DOCMOSIS_TEMPLATES_ENDPOINT = "${var.docmosis_templates_uri}"
    DOCMOSIS_TEMPLATES_ENDPOINT_AUTH = "${data.azurerm_key_vault_secret.docmosis_templates_auth.value}"

    WEBSITE_DNS_SERVER                                    = "${var.dns_server}"
  }
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
  common_tags  = "${var.common_tags}"
}

provider "vault" {
  address = "https://vault.reform.hmcts.net:6200"
}

data "azurerm_key_vault_secret" "docmosis_access_key" {
  name      = "docmosis-access-key"
  vault_uri = "https://rpa-${local.local_env}.vault.azure.net/"
}

data "azurerm_key_vault_secret" "docmosis_templates_auth" {
  name      = "docmosis-templates-auth"
  vault_uri = "https://rpa-${local.local_env}.vault.azure.net/"
}

data "azurerm_key_vault_secret" "s2s_key" {
  name      = "microservicekey-dg-docassembly-api"
  vault_uri = "https://s2s-${local.local_env}.vault.azure.net/"
}

data "azurerm_key_vault" "local_key_vault" {
  name = "${local.vaultName}"
  resource_group_name = "${local.vaultName}"
}

resource "azurerm_key_vault_secret" "local_s2s_key" {
  name         = "microservicekey-dg-docassembly-api"
  value        = "${data.azurerm_key_vault_secret.s2s_key.value}"
  key_vault_id = "${data.azurerm_key_vault.local_key_vault.id}"
}
