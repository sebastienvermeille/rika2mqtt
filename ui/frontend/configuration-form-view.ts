import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-form-layout/vaadin-form-layout.js';
import '@vaadin/vaadin-form-layout/vaadin-form-item.js';
import '@vaadin/email-field/src/vaadin-email-field.js';
import '@vaadin/password-field/src/vaadin-password-field.js';
import '@vaadin/form-layout/src/vaadin-form-item.js';
import '@vaadin/time-picker/src/vaadin-time-picker.js';
import '@vaadin/text-field/src/vaadin-text-field.js';

@customElement('configuration-form-view')
export class ConfigurationFormView extends LitElement {
  // static get styles() {
  //   return css`
  //     :host {
  //         display: block;
  //         height: 100%;
  //     }
  //     `;
  // }

  render() {
    return html`
      <vaadin-form-layout>
       <vaadin-form-item>
        <vaadin-email-field type="email" label="Email"></vaadin-email-field>
       </vaadin-form-item>
       <vaadin-form-item>
        <vaadin-password-field type="password" minlength="8" label="Password"></vaadin-password-field>
       </vaadin-form-item>
       <vaadin-form-item>
        <vaadin-number-field min="15" max="300" step="1" has-controls="true" label="Keep alive interval (seconds)"></vaadin-number-field>
       </vaadin-form-item>
       <vaadin-form-item>
       <vaadin-button>Test</vaadin-button>
</vaadin-form-item>
      </vaadin-form-layout>
    `;
  }

  // Remove this method to render the contents of this view inside Shadow DOM
  createRenderRoot() {
    return this;
  }
}
