<template>
  <div>
    <h1>Demo</h1>
    <p>Below you can see Vue2-Dropzone in action.</p>
    <vue-dropzone ref="myVueDropzone" id="dropzone"

                  :options="dropzoneOptions"
                  :duplicateCheck="true">
    </vue-dropzone>

  </div>
</template>

<script>
  import vueDropzone from '@/components/Dropzone';
  var example = '';
  export default {
    data() {
      return {
        ok: true,
        example: "````" + example + "````",
        dropzoneOptions: {
          url: 'https://httpbin.org/post',
          thumbnailWidth: 150,
          maxFilesize: 0.5,
          headers: { "My-Awesome-Header": "header value" },
          addRemoveLinks: true,
          autoProcessQueue: false,
          accept(file, done) {
            done();
          },
        },

      }
    },
    components: {
      vueDropzone,
    },
    methods: {

    },
    watch: {

    }
  }
</script>

<style scoped>
  .active {
    color: #78CB5B;
  }

  .inactive {
    color: #fff000;
  }

  .fa.fa-circle:before {
    content: "\25C9";
    /*color: #000;*/
  }

  th {
    text-align: center;
  }

  td:nth-child(1) {
    text-align: center;
  }

  td:nth-child(3) {
    text-align: center;
  }

  td:nth-child(2) {
    padding-left: 40px;
  }

  .event-active {
    font-weight: bold;
    color: #78CB5B;
    text-transform: uppercase;
    letter-spacing: 1.2px;
  }
</style>
