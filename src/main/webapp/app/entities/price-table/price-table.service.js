(function() {
    'use strict';
    angular
        .module('wsLivraisonApp')
        .factory('PriceTable', PriceTable);

    PriceTable.$inject = ['$resource'];

    function PriceTable ($resource) {
        var resourceUrl =  'api/price-tables/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
