@echo off
chcp 65001 >nul
setlocal

rem ============================================================
rem  一键推送脚本 —— 基于 Spring Boot 的生鲜平台销售系统
rem  目标仓库: https://github.com/Balrog-89757/fresh-platform
rem
rem  双击本文件即可把本地改动推送到 GitHub。
rem  说明: 本机原本没有 Git，仓库使用便携版 Git (.tools\mingit)，
rem        并通过 SSH 部署密钥认证（无需输入密码）。
rem ============================================================

set "REPO=%~dp0"
set "GIT=%REPO%.tools\mingit\cmd\git.exe"

if not exist "%GIT%" (
    echo [错误] 找不到便携版 Git: %GIT%
    echo        请确认 .tools\mingit 目录仍然存在。
    pause
    exit /b 1
)

echo ============================================
echo   推送到 GitHub: Balrog-89757/fresh-platform
echo ============================================
echo.

cd /d "%REPO%"

echo [1/3] 当前状态:
"%GIT%" status --short --branch
echo.

echo [2/3] 暂存所有改动并提交...
"%GIT%" add -A
"%GIT%" diff --cached --quiet
if errorlevel 1 (
    "%GIT%" commit -m "update: 同步本地改动 %date% %time%"
    if errorlevel 1 (
        echo [错误] 提交失败，请检查上方输出。
        pause
        exit /b 1
    )
) else (
    echo       没有需要提交的新改动。
)
echo.

echo [3/3] 推送到远程...
"%GIT%" push origin main
if errorlevel 1 (
    echo.
    echo [错误] 推送失败。常见原因:
    echo        - 网络波动 / github.com 被阻断（SSH 通道通常可用）
    echo        - 部署密钥被删除或未勾选 Allow write access
    echo        仓库密钥页: https://github.com/Balrog-89757/fresh-platform/settings/keys
    pause
    exit /b 1
)

echo.
echo ============================================
echo   推送成功!  https://github.com/Balrog-89757/fresh-platform
echo ============================================
pause
