import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const config: Config = {
  title: 'RIKA2MQTT',
  tagline: 'Dinosaurs are cool',
  favicon: 'img/favicon.ico',

  url: 'https://rika2mqtt.cookiecode.dev',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: '/',
  trailingSlash: false,

  plugins: [require.resolve("docusaurus-plugin-image-zoom")],

  // GitHub pages deployment config.
  organizationName: 'sebastienvermeille',
  projectName: 'rika2mqtt',
  deploymentBranch: 'gh-pages',

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          routeBasePath: '/',
          sidebarPath: './sidebars.ts',
          editUrl:
            'https://github.com/sebastienvermeille/rika2mqtt/tree/master/docs/docs',
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themeConfig: {
    navbar: {
      title: 'RIKA2MQTT',
      logo: {
        alt: 'RIKA2MQTT Logo',
        src: 'assets/rika2mqtt.png',
      },
      items: [
        {
          href: 'https://github.com/sebastienvermeille/rika2mqtt',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
    },
    algolia: {
      appId: 'E1W6T35JS9',
      apiKey: '98471285c8a136a9d660a78e4e523445',
      indexName: 'rika2mqtt',

      // Optional: see doc section below
      contextualSearch: true,

      // Optional: Algolia search parameters
      searchParameters: {},

      // Optional: path for search page that enabled by default (`false` to disable it)
      searchPagePath: 'search',

    },
  } satisfies Preset.ThemeConfig,
};

export default config;
